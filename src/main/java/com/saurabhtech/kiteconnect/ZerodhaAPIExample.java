package com.saurabhtech.kiteconnect;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Position;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import redis.clients.jedis.Jedis;
import com.google.gson.Gson;

public class ZerodhaAPIExample {

    public static void main(String[] args) {
       try {
            // Initialize KiteConnect with your API key

                    /**public getLoginAccess(KiteConnect kiteConnect) throws IOException, KiteException {
                System.out.println(" About to initialize KiteConnect Object");
                KiteConnect kiteConnect = new KiteConnect("8aeqdeepvi58lkgo");

                // Set user ID
                System.out.println(" About to Set User ID");
                kiteConnect.setUserId("ZC9825");
                return kiteConnect;

            }*/

            System.out.println(" About to initialize KiteConnect Object");
            KiteConnect kiteConnect = new KiteConnect("8aeqdeepvi58lkgo");

            // Get login URL
            String loginUrl = kiteConnect.getLoginURL();
            System.out.println("Login URL: " + loginUrl);

            // Prompt user to enter request_token
            System.out.print("Paste the request_token from the browser URL here: ");
            Scanner scanner = new Scanner(System.in);
            String requestToken = scanner.nextLine();

            try {
                // User generateSession takes the AccessToken generate from the loin URL and the API secret key
                User user = kiteConnect.generateSession(requestToken, "opgo9d6nl6cifwucz89jrk3dm829ilfc");

                // Generate session using request token and API secret
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                System.out.println("Access Token: " + user.accessToken);
                System.out.println("Public Token: " + user.publicToken);


                //System.out.println("Get Holdings: " + kiteConnect.getHoldings());
                System.out.println("About to Print Holdings");                
                // After successful login/session:
                List<Holding> holdingsKite = kiteConnect.getHoldings();
                for (Holding h : holdingsKite) {
                    System.out.println("Symbol: " + h.tradingSymbol + ", Quantity: " + h.quantity + ", Average Price: " + h.averagePrice);
                }

                
                Gson gson = new Gson();

                System.out.println("JSON_HOLDINGS_START");
                System.out.println(gson.toJson(holdingsKite));
                System.out.println("JSON_HOLDINGS_END");

                System.out.println("About to Print Positions");                
                // After successful login/session:
                Map<String, List<Position>> positionsMap = kiteConnect.getPositions();
                List<Position> positionsKite = positionsMap.get("net");            
                    for (Position p : positionsKite) {
                    System.out.println("Symbol: " + p.tradingSymbol + ", Quantity: " + p.netQuantity + ", Average Price: " + p.averagePrice);
                }

                System.out.println("JSON_POSITIONS_START");
                System.out.println(gson.toJson(positionsKite));
                System.out.println("JSON_POSITIONS_END");

            } catch (Exception e) {
                System.out.println("Error generating session Exception: " + e.getMessage());
            } catch (KiteException e) {
                System.out.println("Error generating session KiteException: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("hey Code, are you coming here? " + e.getMessage());
        }
    }
}