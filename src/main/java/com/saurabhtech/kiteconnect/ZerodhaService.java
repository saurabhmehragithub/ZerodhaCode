package com.saurabhtech.kiteconnect;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZerodhaService {
    private static final String API_KEY = "8aeqdeepvi58lkgo";
    private static final String API_SECRET = "opgo9d6nl6cifwucz89jrk3dm829ilfc";
    private static final String USER_ID = "ZC9825";

    private String accessToken;
    private String publicToken;
    private KiteConnect kiteConnect;

    @Autowired
    private HoldingRepository holdingRepository;

    public String getLoginUrl() {
        if (kiteConnect == null) {
            kiteConnect = new KiteConnect(API_KEY);
            kiteConnect.setUserId(USER_ID);
        }
        return kiteConnect.getLoginURL();
    }

    // Call this once per session (or automate with refresh tokens)
    public boolean authenticate(String requestToken) throws Exception {

        try {
                User user = kiteConnect.generateSession(requestToken, API_SECRET);
                accessToken = user.accessToken;
                publicToken = user.publicToken;
                kiteConnect.setAccessToken(accessToken);
                kiteConnect.setPublicToken(publicToken);
                
                return true;

            } catch (Exception e) {
                 System.out.println("Error generating session Exception: " + e.getMessage());
                 return false;
              } 
              catch (KiteException e) {
                 System.out.println("Error generating session KiteException: " + e.getMessage());
                 return false;
              }
        
     }

    private void ensureAuthenticated() throws Exception {
        if (kiteConnect == null || accessToken == null) {
            //authenticate();
        }
    }

    public List<Holding> getHoldings() throws Exception, KiteException { 
         //ensureAuthenticated();
        // Save Holdings to DB and returning 
        saveHoldingsToDb();
        return kiteConnect.getHoldings();


    }

    public List<Position> getPositions() throws Exception, KiteException {
        ensureAuthenticated();
        Map<String, List<Position>> positionsMap = kiteConnect.getPositions();
        return positionsMap.get("net");
    }


    public void saveHoldingsToDb() throws Exception, KiteException {
        //ensureAuthenticated();
        List<Holding> holdings = kiteConnect.getHoldings();
        for (Holding h : holdings) {
            HoldingEntity entity = new HoldingEntity();
            entity.setTradingSymbol(h.tradingSymbol);
            entity.setQuantity(h.quantity); // or h.t1Quantity if that's what you want
            entity.setLastPrice(h.lastPrice);
            holdingRepository.save(entity);
        }
    }

    //@Cacheable("holdings")
    public List<HoldingEntity> getHoldingsFromDb() {
    //System.out.println("If this is called then Caching didn't work as expected PERHAPS not sure");
    //return holdingRepository.findAll();

    List<HoldingEntity> holdings = holdingRepository.findAll();
    System.out.println(holdings); // This will print the list
    return holdings;
}

}