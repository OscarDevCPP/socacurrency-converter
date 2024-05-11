package com.alura.app.utils;

import com.alura.app.App;
import com.alura.app.Constants;
import com.alura.app.exceptions.NotFoundResourceFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CoreHelpers {

	public static CompletableFuture<HttpResponse<String>> httpGetAsync(String url) {
		try (HttpClient client = HttpClient.newHttpClient()) {
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.build();
			 return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
		}
	}

	public static JsonObject toJsonObject(String jsonString) {
		return JsonParser.parseString(jsonString).getAsJsonObject();
	}

	public static String makeApiUrl(String... routeSegments) {
		return String.join("/", routeSegments);
	}


	public static <T> T convertFromJson(String jsonFileDir, Type type) throws IOException {
		Gson gson = new GsonBuilder().create();
		String jsonFromFile = new String(Files.readAllBytes(Paths.get(jsonFileDir)));
		return gson.fromJson(jsonFromFile, type);
	}

	public static String getAssetFile(String asset) throws Exception {
		String assetsClassPath = String.join("/", Constants.PROJECT_CLASSPATH, "assets");
		String resourceClassPath = String.join("/", assetsClassPath, asset);
		Optional<URL> result = Optional.ofNullable(App.class.getResource(resourceClassPath));
		if (result.isEmpty())
			throw new NotFoundResourceFile(String.format("%s in %s", asset, assetsClassPath));
		return result.get().toURI().getPath();
	}

	public static <T> Type getListType(Class<T> classType) {
		return TypeToken.getParameterized(List.class, classType).getType();
	}

	public static <T> List<T> getListFromJson(String jsonFileDir, Class<T> classType) throws IOException {
		return CoreHelpers.convertFromJson(jsonFileDir, CoreHelpers.getListType(classType));
	}

	public static DecimalFormat getDecimalCurrencyFormat(){
		return new DecimalFormat("#.####");
	}

	public static String currencyFormat(double amount){
		return getDecimalCurrencyFormat().format(amount);
	}

	public static double currencyFormat(String amount) throws ParseException {
		if (!amount.matches("^\\d*\\.?\\d*$")) {
			throw new ParseException("Invalid amount: " + amount + ", it should be a valid number", 0);
		}
		return getDecimalCurrencyFormat().parse(amount).doubleValue();
	}

	public static Font loadFont(String fontFilename, float size) throws IOException, FontFormatException, NotFoundResourceFile {
		String fontsClassPath = String.join("/", Constants.PROJECT_CLASSPATH, "fonts");
		String resourceClassPath = String.join("/", fontsClassPath, fontFilename);
		Optional<URL> result = Optional.ofNullable(App.class.getResource(resourceClassPath));
		if (result.isEmpty())
			throw new NotFoundResourceFile(String.format("%s in %s", fontFilename, fontsClassPath));
		InputStream fontInputStream = result.get().openStream();
		Font customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontInputStream));
		customFont = customFont.deriveFont(size);
		fontInputStream.close();
		return customFont;
	}
}
