package com.saurabhtech.kiteconnect;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.cache.Cache;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ZerodhaRestController {

    @Autowired
    private ZerodhaService zerodhaService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/login-url")
    public Map<String, String> getLoginUrl() {
        String url = zerodhaService.getLoginUrl();
        System.out.println("Login URL: in Rest Controller " + url);
        return Map.of("loginUrl", url);
    }

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestBody Map<String, String> body) throws Exception {
        try {
            String requestToken = body.get("requestToken");
            System.out.println("requestToken in Rest Controller is" + requestToken);
            zerodhaService.authenticate(requestToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/holdings")
    public List<Holding> getHoldings() throws Exception, KiteException {
        System.out.println("FIRST  ZerodhaRestController | Print from the DB : " ); getHoldingsFromDb();
        System.out.println("SECOND ZerodhaRestController | Print from cache function and see : " + printHoldingsCache());
        return zerodhaService.getHoldings();
    }

    @GetMapping("/positions")
    public List<Position> getPositions() throws Exception, KiteException {
        return zerodhaService.getPositions();
    }
                         
    @GetMapping("/getholdingsdb")
    public List<HoldingEntity> getHoldingsFromDb() {
        return zerodhaService.getHoldingsFromDb();
    }

     @GetMapping("/printHoldingsCache")
    public Object printHoldingsCache() {
        Cache cache = cacheManager.getCache("holdings");
        if (cache == null) {
            return "Cache 'holdings' not found.";
        }
        // For simple cache, keys are usually method parameters (here, likely 'SimpleKey []')
        // If you know the key, you can get the value:
        Object value = cache.get(SimpleKey.EMPTY); // Replace with actual key if needed
        return value != null ? "Cached Value available "  : "No value cached for key.";
    }

}