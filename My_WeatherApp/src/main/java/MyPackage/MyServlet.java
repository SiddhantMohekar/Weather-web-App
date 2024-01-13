package MyPackage;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputData = request.getParameter("userInput");
		
		
		// API Setup
		String apikey = "96310aca531ab6d367cdce2a12a65049";
		// get city from user input
		String city = request.getParameter("city");
		// create the URL for the OpenWeatherMap API request
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey;
		
		//API Integration 
		URL url = new URL(apiUrl);
		// connection of api
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//Reading data from network
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		//want to store in string
		StringBuilder responseContent = new StringBuilder();
		
		//Input lene ke liye from the reader , will create scanner object
		Scanner scanner = new Scanner(reader);
		
		// scanner.hasNext() - run until the last line
		while(scanner.hasNext()){
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		
//		System.out.println(responseContent);
		
		// TypeCasting - parsing data into JSON
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
//		System.out.println(jsonObject);
		
		//Date & Time
		//here we get date in milisec
		long dateTimestamp = jsonObject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimestamp).toString();
		
		//Temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind Speed 
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Condition
		String weatherCondition =jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		
		//Set the date as request attributes (for sending to the jsp page)
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherDate", responseContent.toString());
		
		connection.disconnect();
		// Forward the request to the weather.jsp page for rendering 
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
         
}
