package ru.icecubenext.kanban.managers.impl.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

@Log4j
public class KVServer {
	public static final int PORT = 8078;
	private final String apiToken;
	private final HttpServer server;
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private final Map<String, String> data = new HashMap<>();

	public KVServer() throws IOException {
		apiToken = generateApiToken();
		server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
		server.createContext("/register", this::register);
		server.createContext("/save", this::save);
		server.createContext("/load", this::load);
	}

	private void load(HttpExchange h) throws IOException {
		try {
			if (!hasAuth(h)) {
				log.debug("Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				h.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
				return;
			}
			if ("GET".equals(h.getRequestMethod())) {
				String key = h.getRequestURI().getPath().substring("/load/".length());
				if (key.isEmpty()) {
					log.debug("Key для запроса пустой. Key указывается в пути: /load/{key}");
					h.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				if (data.containsKey(key)) {
					String responseString = data.get(key);
					byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
					h.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
					try (OutputStream os = h.getResponseBody()) {
						os.write(bytes);
					}
				} else {
					log.debug("Данные по запрашиваемому ключу отсутствуют. key=" + key);
					h.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
				}
			}
		} finally {
			h.close();
		}
	}

	private void save(HttpExchange h) throws IOException {
		try {
			if (!hasAuth(h)) {
				log.debug("Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				h.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
				return;
			}
			if ("POST".equals(h.getRequestMethod())) {
				String key = h.getRequestURI().getPath().substring("/save/".length());
				if (key.isEmpty()) {
					log.debug("Key для сохранения пустой. key указывается в пути: /save/{key}");
					h.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				String value = readText(h);
				if (value.isEmpty()) {
					log.debug("Value для сохранения пустой. value указывается в теле запроса");
					h.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				if (data.containsKey(key) && data.get(key).equals(value)) {
					h.sendResponseHeaders(HttpURLConnection.HTTP_NOT_MODIFIED, -1);
				} else {
					data.put(key, value);
					log.debug("Значение для ключа " + key + " успешно обновлено!");
					h.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				}
			} else {
				log.debug("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
				h.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
			}
		} finally {
			h.close();
		}
	}

	private void register(HttpExchange h) throws IOException {
		try {
			log.debug("Регистрирую клиента. Клиенту присвоен API_TOKEN: " + apiToken);
			if ("GET".equals(h.getRequestMethod())) {
				sendText(h, apiToken);
			} else {
				log.debug("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
				h.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
			}
		} finally {
			h.close();
		}
	}

	public void start() {
		log.debug("Запускаем сервер: http://localhost:" + PORT + "/");
		server.start();
	}

	public void stop() {
		log.debug("Закрываем сервер на порту " + PORT);
		server.stop(1);
	}

	private String generateApiToken() {
		return "" + System.currentTimeMillis();
	}

	protected boolean hasAuth(HttpExchange h) {
		String rawQuery = h.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
	}

	protected String readText(HttpExchange h) throws IOException {
		return new String(h.getRequestBody().readAllBytes(), UTF_8);
	}

	protected void sendText(HttpExchange h, String text) throws IOException {
		byte[] resp = text.getBytes(UTF_8);
		h.getResponseHeaders().add("Content-Type", "application/json");
		h.sendResponseHeaders(HttpURLConnection.HTTP_OK, resp.length);
		h.getResponseBody().write(resp);
	}
}
