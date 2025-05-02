package co.edu.unbosque.springfirstapp.service;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;

import java.net.http.HttpResponse;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();


}