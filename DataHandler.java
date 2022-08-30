package ca.yorku.eecs;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataHandler implements HttpHandler, AutoCloseable {
	private Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"), Config.builder().withoutEncryption().build());
	
	
	@Override
	public void handle(HttpExchange request) throws IOException {
		String urlString = request.getRequestURI().toString();
		
		try {
			switch(urlString) {
				case "/api/v1/addActor":
					addActor(request);
					break;
				case "/api/v1/addMovie":
					addMovie(request);
					break;
				case "/api/v1/addRelationship":
					addRelationship(request);
					break;
				case "/api/v1/addMovieYear":
					addMovieYear(request);
					break;
				case "/api/v1/addIMDbRating":
					addIMDbRating(request);
					break;
				case "/api/v1/addMPAARating":
					addMPAARating(request);
					break;
				case "/api/v1/getActor":
					getActor(request);
					break;
				case "/api/v1/getMovie":
					getMovie(request);
					break;
				case "/api/v1/hasRelationship":
					hasRelationship(request);
					break;
				case "/api/v1/computeBaconNumber":
					computeBaconNumber(request);
					break;
				case "/api/v1/computeBaconPath":
					computeBaconPath(request);
					break;
				case "/api/v1/getMovieYear":
					getMovieYear(request);
					break;
				case "/api/v1/getIMDbRating":
					getIMDbRating(request);
					break;
				case "/api/v1/getMPAARating":
					getMPAARating(request);
					break;
				default:
					request.sendResponseHeaders(400, 0);
					
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject getJson(HttpExchange request) throws IOException, JSONException {
		byte[] data = new byte[1000];
		String stringData = "";
		
		InputStream is = request.getRequestBody();
		is.read(data);
		is.close();
		
		byte[] newData = new byte[data.length];
		newData = data;
		stringData = new String(newData);
		
		JSONObject dataJson = new JSONObject(stringData);
		
		return dataJson;
	}
	
	private void sendStatusCode(HttpExchange request, int code, int length) throws IOException {
		request.sendResponseHeaders(code, length);
		request.close();
	}
	
	private void addActor(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			String name = (String) dataJson.get("name");
			String actorId = (String) dataJson.get("actorId");
			
			StatementResult idExist;
			
			if (name == "" || name == null || actorId == "" || actorId == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
		        		StatementResult node_boolean = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId) );
		        		idExist = node_boolean;
					}
					if (!idExist.hasNext()) {
		    			session.writeTransaction(tx -> tx.run( "MERGE (a:actor {id: $ai, Name: $nm})", parameters("ai", actorId, "nm", name)));
		    			session.close();
		    			sendStatusCode(request, 200, 0);
		    		}
		    		else
		    			sendStatusCode(request, 400, 0);
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
		
	}

	private void addMovie(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			String name = (String) dataJson.get("name");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult idExist;
			
			if (name == "" || name == null || movieId == "" || movieId == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
		        		StatementResult node_boolean = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
		        		idExist = node_boolean;
					}
					if (!idExist.hasNext()) {
						session.writeTransaction(tx -> tx.run("MERGE (m:movie {id: $mi, Name: $nm})", parameters("mi", movieId, "nm", name)));
						session.close();
						sendStatusCode(request, 200, 0);
		    		}
		    		else
		    			sendStatusCode(request, 400, 0);
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void addRelationship(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			String actorId = (String) dataJson.get("actorId");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult movie, actor;
			
			if (actorId == "" || actorId == null || movieId == "" || movieId == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
						movie = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
						actor = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId));
					}
					if (movie.hasNext() == false || actor.hasNext() == false) {
						sendStatusCode(request, 404, 0);
					}
					else {
						session.writeTransaction(tx -> tx.run("MATCH (a:actor {id: $ai}), (m:movie {id: $mi})\n" +
															  "MERGE (a)-[r:ACTED_IN]->(m)\n" + "RETURN r" , parameters("ai", actorId, "mi", movieId)));
						session.close();
						sendStatusCode(request, 200, 0);
					}
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void addMovieYear(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			int year = (int) dataJson.get("year");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult movie;
			
			if(year <= 0 || movieId == null || movieId == "") {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
						movie = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
					}
					if (movie.hasNext() == false) {
						sendStatusCode(request, 404, 0);
					}
					else {
						session.writeTransaction(tx -> tx.run("MATCH (n:movie {id: $im}) SET n.year = $y" , parameters("im", movieId, "y", year)));
						session.close();
						sendStatusCode(request, 200, 0);
					}
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void addIMDbRating(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			int IMDbRating = (int) dataJson.get("IMDbRating");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult movie;
			
			if (IMDbRating < 1 || IMDbRating > 10 || movieId == null || movieId == "") {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
						movie = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
					}
					if (movie.hasNext() == false) {
						sendStatusCode(request, 404, 0);
					}
					else {
						session.writeTransaction(tx -> tx.run("MATCH (n:movie {id: $im}) SET n.IMDbRating = $y" , parameters("im", movieId, "y", IMDbRating)));
						session.close();
						sendStatusCode(request, 200, 0);
					}
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void addMPAARating(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			String MPAARating = (String) dataJson.get("MPAARating");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult movie;
			
			if (MPAARating == "" || MPAARating == null || movieId == "" || movieId == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
					try(Transaction tx = session.beginTransaction()) {
						movie = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
					}
					if (movie.hasNext() == false) {
						sendStatusCode(request, 404, 0);
					}
					else {
						session.writeTransaction(tx -> tx.run("MATCH (n:movie {id: $im}) SET n.MPAARating = $y" , parameters("im", movieId, "y", MPAARating)));
						session.close();
						sendStatusCode(request, 200, 0);
					}
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
				}
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void getActor(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try { 
			JSONObject actor = new JSONObject();
			JSONArray moviesArray = new JSONArray();
			Map<Object, Object> movieObject = new LinkedHashMap<>();
			
			String actorId = (String) dataJson.get("actorId");
			
			StatementResult name, movie, actorExist;
			
			if (actorId == null || actorId == "") {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
		        	try (Transaction tx = session.beginTransaction()) {
		        		actorExist = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId) );
		        		
		        		if (actorExist.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
			        		name = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.Name", parameters("ai", actorId));
			        		movie = tx.run("MATCH (a:actor {id: $ai})-[ACTED_IN]->(movie) RETURN movie.id", parameters("ai", actorId));
			        		
			        		if (movie.hasNext()) {
			        			while (movie.hasNext()) {
				        			moviesArray.put(movie.next().get("movie.id").toString().replaceAll("\"", ""));
			        			}
			        		}
			        		
			        		String actorName = name.next().get("n.Name").toString();
			        		
			        		movieObject.put("actorId", actorId);
			        		movieObject.put("name", actorName.replaceAll("\"", ""));
			        		movieObject.put("movies", moviesArray);
			        		
			        		actor = new JSONObject(movieObject);
			        		
			        		String actorStr = actor.toString();
			    			request.sendResponseHeaders(200, actorStr.length());
			    		    OutputStream os = request.getResponseBody();
			    		    os.write(actorStr.getBytes());
			    		    os.close();
			    		    request.close();
		        		}
		        	} catch (Exception err) {
		        		sendStatusCode(request, 500, 0);
			        }
		        } catch (Exception err) {
		        	sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void getMovie(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject movie = new JSONObject();
			JSONArray actorsArray = new JSONArray();
			Map<Object, Object> actorObject = new LinkedHashMap<>();
			
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult name, actor, movieExist;
			
			if (movieId == null || movieId == "") {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
		        	try (Transaction tx = session.beginTransaction()) {
		        		movieExist = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
		        		
		        		if (movieExist.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
			        		name = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.Name", parameters("mi", movieId));
			        		actor = tx.run("MATCH (m:movie {id: $mi})<-[ACTED_IN]-(actor) RETURN actor.id", parameters("mi", movieId));
			        		
			        		if (actor.hasNext()) {
			        			while (actor.hasNext()) {
				        			actorsArray.put(actor.next().get("actor.id").toString().replaceAll("\"", ""));
			        			}
			        		}
			        		
			        		String movieName = name.next().get("n.Name").toString();
			        		
			        		actorObject.put("movieId", movieId);
			        		actorObject.put("name", movieName.replaceAll("\"", ""));
			        		actorObject.put("actors", actorsArray);
			        		
			        		movie = new JSONObject(actorObject);
			        		
			        		String movieStr = movie.toString();
			        		request.sendResponseHeaders(200, movieStr.length());
			        		OutputStream os = request.getResponseBody();
			        		os.write(movieStr.getBytes());
			        		os.close();
			        		request.close();
		        		}
		        	} catch (Exception err) {
		        		sendStatusCode(request, 500, 0);
			        }
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}	
	}
	
	private void hasRelationship(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject relation = new JSONObject();
			boolean isRelation = true;
			Map<Object, Object> relObject = new LinkedHashMap<>();
			
			String actorId = (String) dataJson.get("actorId");
			String movieId = (String) dataJson.get("movieId");
			
			StatementResult movie, actor, relationship;
			
			if (actorId == "" || actorId == null || movieId == "" || movieId == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()) {
		        	try (Transaction tx = session.beginTransaction()) {
		        		movie = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.id", parameters("mi", movieId));
						actor = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId));
						
						if (movie.hasNext() == false || actor.hasNext() == false) {
							sendStatusCode(request, 404, 0);
						}
						else {
			        		relationship = tx.run("MATCH (n:movie) WHERE n.id = $mi RETURN n.Name", parameters("mi", movieId));
			        		
			        		if (relationship.hasNext()) 
			        			isRelation = true;
			        		else
			        			isRelation = false;
			        	
			        		relObject.put("actorId", actorId);
			        		relObject.put("movieId", movieId);
			        		relObject.put("hasRelationship", isRelation);
			        		
			        		relation = new JSONObject(relObject);
			        		
			        		String realtionStr = relation.toString();
							request.sendResponseHeaders(200, realtionStr.length());
						    OutputStream os = request.getResponseBody();
						    os.write(realtionStr.getBytes());
						    os.close();
						    request.close();
						}
		        	}
		        	catch (Exception err) {
		        		sendStatusCode(request, 500, 0);
			        }
				} catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
		
	}
	
	private void computeBaconNumber(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject baconNum = new JSONObject();
			
			String actorId = (String) dataJson.get("actorId");
			
			StatementResult actor, bacon;
			
			if (actorId == null || actorId == "") {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()){
		        	try (Transaction tx = session.beginTransaction()) {
						actor = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId));
						
						if (actor.hasNext() == false) {
							sendStatusCode(request, 404, 0);
						}
						else {
			        		bacon = tx.run("MATCH path = shortestPath((k:actor {id: 'nm0000102'})-[:ACTED_IN*]-(n:actor {id: $ai})) RETURN length(path)", parameters("ai", actorId));
			        		
			        		String baconString = bacon.next().get("length(path)").toString();
			        		
			        		baconNum.put("baconNumber", Integer.parseInt(baconString));
			        		
			        		String baconNumStr = baconNum.toString();
			    			request.sendResponseHeaders(200, baconNumStr.length());
			    		    OutputStream os = request.getResponseBody();
			    		    os.write(baconNumStr.getBytes());
			    		    os.close();
			    		    request.close();
						}
		        	} catch (Exception err) {
						sendStatusCode(request, 500, 0);
			        } 
		        } catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void computeBaconPath(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject baconPath = new JSONObject();
			JSONArray baconArray = new JSONArray();
			
			String actorId = (String) dataJson.get("actorId");
			
			StatementResult movie, actor, bacon;
			
			try (Session session = driver.session()) {
	        	try (Transaction tx = session.beginTransaction()) {
					actor = tx.run("MATCH (n:actor) WHERE n.id = $ai RETURN n.id", parameters("ai", actorId));
					
					if (actor.hasNext() == false) {
						sendStatusCode(request, 404, 0);
					}
					else {
		        		bacon = tx.run("MATCH path = shortestPath((n:actor {id: $ai})-[:ACTED_IN*]-(k:actor {id: 'nm0000102'})) UNWIND nodes(path) as a RETURN a.id", parameters("ai", actorId));
		        		
		        		if (bacon.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
			        		if (bacon.hasNext()) {
			        			while (bacon.hasNext()) {
			        				baconArray.put(bacon.next().get("a.id").toString().replaceAll("\"", ""));
			        			}
			        		}
		        			
			        		baconPath.put("baconPath", baconArray);
			        				
			        		String baconPathStr = baconPath.toString();
			    			request.sendResponseHeaders(200, baconPathStr.length());
			    		    OutputStream os = request.getResponseBody();
			    		    os.write(baconPathStr.getBytes());
			    		    os.close();
			    		    request.close();
		        		}
					}
	    		    
	        	} catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        } 
	        } catch (Exception err) {
				sendStatusCode(request, 500, 0);
	        } 
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}

	private void getMovieYear(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject movie = new JSONObject();
			JSONArray movieArray = new JSONArray();
			Map<Object, Object> movies = new LinkedHashMap<>();
			
			int year = (int) dataJson.get("year");
			
			if (year <= 0) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()){
		        	try (Transaction tx = session.beginTransaction()) {
		        		StatementResult m = tx.run("MATCH (n:movie) WHERE n.year = $y RETURN n.id", parameters("y", year));
		        		
		        		if (m.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
			        		if (m.hasNext()) {
			        			while (m.hasNext()) {
				        			movieArray.put(m.next().get("n.id").toString().replaceAll("\"", ""));
			        			}
			        		}
			        		
			        		movies.put("year", year);
			        		movies.put("movies", movieArray);
			        		
			        		movie = new JSONObject(movies);
			        		
			        		String movieString = movie.toString();
							request.sendResponseHeaders(200, movieString.length());
						    OutputStream os = request.getResponseBody();
						    os.write(movieString.getBytes());
						    os.close();
						    request.close();
		        		}
					    
		        	} catch (Exception err) {
						sendStatusCode(request, 500, 0);
			        } 
		        } catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        } 
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void getIMDbRating(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject movie = new JSONObject();
			JSONArray movieArray = new JSONArray();
			Map<Object, Object> movies = new LinkedHashMap<>();
			
			int IMDbRating = (int) dataJson.get("IMDbRating");
			
			if (IMDbRating < 1 || IMDbRating > 10) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()){
		        	try (Transaction tx = session.beginTransaction()) {
		        		StatementResult m = tx.run("MATCH (n:movie) WHERE n.IMDbRating = $y RETURN n.id", parameters("y", IMDbRating));
		        		
		        		if (m.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
			        		if (m.hasNext()) {
			        			while (m.hasNext()) {
				        			movieArray.put(m.next().get("n.id").toString().replaceAll("\"", ""));
			        			}
			        		}
			        		
			        		movies.put("IMDbRating", IMDbRating);
			        		movies.put("movies", movieArray);
			        		
			        		movie = new JSONObject(movies);
			        		
			        		String movieString = movie.toString();
							request.sendResponseHeaders(200, movieString.length());
						    OutputStream os = request.getResponseBody();
						    os.write(movieString.getBytes());
						    os.close();
						    request.close();
		        		}
		        	} catch (Exception err) {
						sendStatusCode(request, 500, 0);
		        	}
		        } catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	private void getMPAARating(HttpExchange request) throws IOException, JSONException {
		JSONObject dataJson = getJson(request);
		
		try {
			JSONObject movie = new JSONObject();
			JSONArray movieArray = new JSONArray();
			Map<Object, Object> movies = new LinkedHashMap<>();
			
			String MPAARating = (String) dataJson.get("MPAARating");
			
			if (MPAARating == "" || MPAARating == null) {
				sendStatusCode(request, 400, 0);
			}
			else {
				try (Session session = driver.session()){
		        	try (Transaction tx = session.beginTransaction()) {
		        		StatementResult m = tx.run("MATCH (n:movie) WHERE n.MPAARating = $y RETURN n.id", parameters("y", MPAARating));
		        		
		        		if (m.hasNext() == false) {
		        			sendStatusCode(request, 404, 0);
		        		}
		        		else {
		        			if (m.hasNext()) {
			        			while (m.hasNext()) {
			        				movieArray.put(m.next().get("n.id").toString().replaceAll("\"", ""));
			        			}
		        			}
		        		
			        		movies.put("MPAARating", MPAARating);
			        		movies.put("movies", movieArray);
			        		
			        		movie = new JSONObject(movies);
			        		
			        		String movieString = movie.toString();
			    			request.sendResponseHeaders(200, movieString.length());
			    		    OutputStream os = request.getResponseBody();
			    		    os.write(movieString.getBytes());
			    		    os.close();
			    		    request.close();
		        		}
		        	} catch (Exception err) {
						sendStatusCode(request, 500, 0);
			        }
		        } catch (Exception err) {
					sendStatusCode(request, 500, 0);
		        }
			}
		} catch(JSONException err) {
			sendStatusCode(request, 400, 0);
		}
	}
	
	@Override
	public void close() throws Exception {
		driver.close();
	}
}