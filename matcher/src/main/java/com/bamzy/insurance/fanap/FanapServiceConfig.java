package com.bamzy.insurance.fanap;

import com.bamzy.insurance.ws.caller.WebServiceCaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bamdad
 */
@Configuration
public class FanapServiceConfig {
    private static final String userServicesUrl = "https://modern.ttbank.ir/WebServices/UserServices.asmx";
    private static final String brokersServiceUrl = "https://modern.ttbank.ir/WebServices/BrokersService.asmx";
    private static final String userServicesNamespace = "http://ibank.toranj.fanap.co.ir/UserServices";
    private static final String brokersServiceNamespace = "http://ibank.toranj.fanap.co.ir/";
    private static final String username = "1198864Service";
    private static final String brokerId = "10001";
    private static final String modulus = "35TQ2aGSkgsWKqyUvLNrZepurQBLjO7PK3v2B1rthxY1Vgngr5WMn1HBO8bAaEHTWpPho5EWVrM8o2ywTWnRj20nnIrL7zbHg1DP3febrzyUY3uTDR5cXHOP1R4EJAiX0r1ClrbEaQgGvegtEq2umqFPVdL+TimxRO33MU4r75gSac+YHB/lIEslQDUZeO/fKdiHX8j+7klDy1OAipXkzzc5RoLLSMya74DsLzjYiUg/cSY2Xwkj+mB6yJa9BBJXX/ykJ7hsO5X3SmeTQySHlU207Vc2WBHfX5xkzvIrQp9C84jTC360zyeUMBIF8N7Im2nK8R+5qQRVaNfBWnBGRw==";
    private static final String exponent = "AQAB";
    private static final String d = "LZ9TQqVqoCxs+IZAz4SZDLHapaiiwf5U/d2MpZGTWHZaqdTYKVwN1tC66nSy/FM6SohUiajocwaeAAcErrdCEM9IPvQ/iu+VpKEP2Y5WEVr+OJKVvh6M4Dygg+9dDvFF8bW26cQfrgL6RTQkRmR0dY8m/i9QvyhYyOT6BPCbnIb8qXdAJTRaeRi5bGrKpO2zxArX8OuFcE9h4QINtZ4t4agN5NS8q0ySZFAz+L1SMWeFtUcL3ZHpsQWX/tSYkbuWpDzpw57rUIo7cHvAw07HCwgN9hlWZG4WFzLuNsQIYgu0m/QZcIcHeEU6lGM32RpVp2y3AW7WAZhk6H14aCPNmQ==";

    @Bean
    public WebServiceCaller webServiceCaller() {
        return new WebServiceCaller(modulus, exponent, d, brokerId, username, userServicesUrl, userServicesNamespace, brokersServiceUrl, brokersServiceNamespace);
    }

}
