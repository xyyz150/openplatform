package qimenapi;


import com.qimenapi.util.HttpHelper;
import com.srop.Constants;
import com.srop.utils.SropUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyyz150 on 2016/1/7.
 */

public class testclient {

    @Test
    public void aaa() throws Exception{
        String appKey="appKey1",appSecret="123456",accessToken="accessToken1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "qs.get");
        map.put("timestamp", "2020-05-30 11:08:49");
        map.put("format", "json");
        map.put("appKey", appKey);
        map.put("v", "2.0");
//        map.put("sign_method", "md5");
        map.put("accessToken", accessToken);
        String body = "{\"code\":\"1111\",\"name\":\"aaaabbbbcccc\"}";
        String sign = SropUtils.sign(map, body, appSecret);
        map.put("sign", sign);
        String url = "http://localhost:8080/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
        String str = HttpHelper.doPost(url, body);
        System.out.print(str);
    }

//
//    @Test
//    public void testtesteee() {
//        try {
//            DeliveryOrderConfirmRequest request = new DeliveryOrderConfirmRequest();
//            DeliveryOrderConfirmRequestDeliveryOrder order = new DeliveryOrderConfirmRequestDeliveryOrder();
//            order.setconfirmType(new Integer(0));
//            order.setdeliveryOrderCode("单号0001");
//            order.setdeliveryOrderId("1q2w3e");
//            order.setwarehouseCode("001");
//            request.setdeliveryOrder(order);
//            String param = JsonUtils.writeValueAsString(request);
//            String json = HttpHelper.doPostJson("http://localhost:8088/router", param);
//            System.out.print(json);
//        } catch (Exception e) {
//            System.out.print(e.toString());
//        }
//    }
//
//    @Test
//    public void testheard() {
//        try {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("method", "service.heartbeat");
//            map.put("timestamp", "2015-02-01 00:00:00");
//            map.put("format", "xml");
//            map.put("app_key", "wms_appkey");
//            map.put("v", "2.0");
//            map.put("sign_method", "md5");
//            map.put("customerId", "QIMENUSR1");
//            ServiceHeartBeatRequest request = new ServiceHeartBeatRequest();
//            request.setSerialNumber("33333");
//            String body = XStreamUtils.parseObj2Xml(request);
//            String appscret = "sandboxfe5ec500237d2851cf6049c98";
//            String sign = SropUtils.sign(map, body, appscret);
//            map.put("sign", sign);
//            String url = "http://localhost:8082/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
//            String str = HttpHelper.doPost(url, body);
//            System.out.print(str);
//        } catch (Exception e) {
//            System.out.print(e.toString());
//        }
//    }

//    @Test
//    public void testxsteam() {
//        try {
//            String body = "<request>\n" +
//                    "\t<returnOrder>\n" +
//                    "\t\t<returnOrderCode>THHD201602170000002</returnOrderCode>\n" +
//                    "\t\t<returnOrderId>2102488914</returnOrderId>\n" +
//                    "\t\t<warehouseCode>KJ-0009</warehouseCode>\n" +
//                    "\t\t<outBizCode>21024889142016-02-17 20:04:59.5372</outBizCode>\n" +
//                    "\t\t<orderType>THRK</orderType>\n" +
//                    "\t\t<orderConfirmTime>2016-02-17 20:07:44</orderConfirmTime>\n" +
//                    "\t\t<returnReason></returnReason>\n" +
//                    "\t\t<logisticsCode></logisticsCode>\n" +
//                    "\t\t<logisticsName></logisticsName>\n" +
//                    "\t\t<expressCode></expressCode>\n" +
//                    "\t\t<senderInfo>\n" +
//                    "\t\t\t<company></company>\n" +
//                    "\t\t\t<name></name>\n" +
//                    "\t\t\t<zipCode></zipCode>\n" +
//                    "\t\t\t<tel></tel>\n" +
//                    "\t\t\t<mobile></mobile>\n" +
//                    "\t\t\t<email></email>\n" +
//                    "\t\t\t<countryCode></countryCode>\n" +
//                    "\t\t\t<province></province>\n" +
//                    "\t\t\t<city></city>\n" +
//                    "\t\t\t<area></area>\n" +
//                    "\t\t\t<town></town>\n" +
//                    "\t\t\t<detailAddress></detailAddress>\n" +
//                    "\t\t</senderInfo>\n" +
//                    "\t</returnOrder>\n" +
//                    "\t<orderLines>\n" +
//                    "\t\t<orderLine>\n" +
//                    "\t\t\t<orderLineNo></orderLineNo>\n" +
//                    "\t\t\t<sourceOrderCode></sourceOrderCode>\n" +
//                    "\t\t\t<subSourceOrderCode></subSourceOrderCode>\n" +
//                    "\t\t\t<itemCode>10020110</itemCode>\n" +
//                    "\t\t\t<itemId>1703092564</itemId>\n" +
//                    "\t\t\t<inventoryType>ZP</inventoryType>\n" +
//                    "\t\t\t<planQty>2</planQty>\n" +
//                    "\t\t\t<actualQty>2</actualQty>\n" +
//                    "\t\t\t<batchCode></batchCode>\n" +
//                    "\t\t\t<productDate></productDate>\n" +
//                    "\t\t\t<expireDate></expireDate>\n" +
//                    "\t\t\t<produceCode></produceCode>\n" +
//                    "\t\t</orderLine>\n" +
//                    "\t</orderLines>\n" +
//                    "</request>\n";
////            String body = "<request>\n" +
////                    "  <deliveryOrder>\n" +
////                    "          <deliveryOrderCode>886548046348</deliveryOrderCode>\n" +
////                    "  </deliveryOrder>\n" +
////                    "</request>\n";
////            String body="<error_response>\n" +
////                    "    <code>50</code>\n" +
////                    "    <msg>Remote service error</msg>\n" +
////                    "    <sub_code>isv.invalid-parameter</sub_code>\n" +
////                    "    <sub_msg>非法参数</sub_msg>\n" +
////                    "</error_response>";
//            //XStream xStream= XStreamUtils.getXstream(com.qs.wms.qimen.domian.DeliveryOrderConfirmRequest.class);
////            xStream.setMode(XStream.NO_REFERENCES);
//            ObjectXmlParser<ReturnOrderConfirmRequest> parser =
//                    new ObjectXmlParser<ReturnOrderConfirmRequest>(ReturnOrderConfirmRequest.class);
//            SropRequest sropRequest = (SropRequest) parser.parse(body);
//            System.out.print(sropRequest);
//        } catch (Exception e) {
//            System.out.print(e.toString());
//        }
//    }

    @Test
    public void testenmu() {
        System.out.print(MainErroreeee.INVALID_APP_KEY.toString());
    }


    @Test
    public void aa() {
        System.out.print("aaa");

        Map<Long, Long> singlewareinventory = new HashMap<Long, Long>();
        singlewareinventory.put(10l, 10l);
        singlewareinventory.put(11l, 11l);
        for (Map.Entry<Long, Long> sku : singlewareinventory.entrySet()) {

            int a = 0;
        }
//        <entryOrder>
//        <entryOrderCode>RKD201602030000002</entryOrderCode>
//        <ownerCode>c1454466745925</ownerCode>
//        <warehouseCode>KJ-0009</warehouseCode>
//        <entryOrderId>2102485767</entryOrderId>
//        <entryOrderType>CGRK</entryOrderType>
//        <outBizCode>21024857672016-02-03 18:46:09.9732</outBizCode>
//        <confirmType>0</confirmType>
//        <status>FULFILLED</status>
//        <operateTime>2016-2-3 18:48:49</operateTime>
//        <remark></remark>
//        </entryOrder>
//        <orderLines>
//        <orderLine>
//        <orderLineNo></orderLineNo>
//        <itemCode>10020114</itemCode>
//        <itemId>1703092562</itemId>
//        <itemName>ZUK手机 Z1标配版 橡木</itemName>
//        <inventoryType>ZP</inventoryType>
//        <planQty>11</planQty>
//        <actualQty>11</actualQty>
//        <batchCode></batchCode>
//        <productDate></productDate>
//        <expireDate></expireDate>
//        <produceCode></produceCode>
//        <remark></remark>
//        </orderLine>
//        <orderLine>
//        <orderLineNo></orderLineNo>
//        <itemCode>12040010</itemCode>
//        <itemId>1703092563</itemId>
//        <itemName>Z1 钢化玻璃贴膜</itemName>
//        <inventoryType>ZP</inventoryType>
//        <planQty>11</planQty>
//        <actualQty>11</actualQty>
//        <batchCode></batchCode>
//        <productDate></productDate>
//        <expireDate></expireDate>
//        <produceCode></produceCode>
//        <remark></remark>
//        </orderLine>
//        <orderLine>
//        <orderLineNo></orderLineNo>
//        <itemCode>10020110</itemCode>
//        <itemId>1703092564</itemId>
//        <itemName>ZUK手机 Z1标配版 白色</itemName>
//        <inventoryType>ZP</inventoryType>
//        <planQty>11</planQty>
//        <actualQty>11</actualQty>
//        <batchCode></batchCode>
//        <productDate></productDate>
//        <expireDate></expireDate>
//        <produceCode></produceCode>
//        <remark></remark>
//        </orderLine>
//        </orderLines>
    }


    @Test
    public void tesetorderqq() {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("method", "orderprocess.report");
            map.put("timestamp", "2015-02-01 00:00:00");
            map.put("format", "xml");
            map.put("app_key", "1023012571");
            map.put("v", "2.0");
            map.put("sign_method", "md5");
            map.put("customerId", "c1454466745925");
            String body = "<request>\n" +
                    "\t<order>\n" +
                    "\t\t<orderCode>DBD201602160000001</orderCode>\n" +
                    "\t\t<orderId>3148750142</orderId>\n" +
                    "\t\t<orderType>DBeeCK</orderType>\n" +
                    "\t\t<warehouseCode>KJ-0009</warehouseCode>\n" +
                    "\t</order>\n" +
                    "\t<process>\n" +
                    "\t\t<processStatus>ACCEPT</processStatus>\n" +
                    "\t\t<operatorCode></operatorCode>\n" +
                    "\t\t<operatorName></operatorName>\n" +
                    "\t\t<operateTime>2016/2/16 10:26:39</operateTime>\n" +
                    "\t\t<operateInfo>接单</operateInfo>\n" +
                    "\t\t<remark></remark>\n" +
                    "\t\t<extendProps>\n" +
                    "\t\t\t<key1></key1>\n" +
                    "\t\t\t<key2></key2>\n" +
                    "\t\t</extendProps>\n" +
                    "\t</process>\n" +
                    "</request>";
            String appscret = "2921843fe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://localhost:8082/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @Test
    public void PurchaseIn()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "entryorder.confirm");
        map.put("timestamp", "2016-09-01 17:36:59");
        map.put("format", "xml");
        map.put("app_key", "1023178000");
        map.put("v", "2.0");
        map.put("sign_method", "md5");
        map.put("customerId", "c1472610250306");
        String body = "<request>\n" +
                "  <entryOrder>\n" +
                "    <totalOrderLines>0</totalOrderLines>\n" +
                "    <entryOrderCode>CGDD201609010000002</entryOrderCode>\n" +
                "    <warehouseCode>47a7377d4a9e42e3a665af0894946e21</warehouseCode>\n" +
                "    <entryOrderId>29a35d5ddb25484f98189170120c76f9</entryOrderId>\n" +
                "    <entryOrderType>CGRK</entryOrderType>\n" +
                "    <outBizCode>29a35d5ddb25484f98189170120c76f9</outBizCode>\n" +
                "    <confirmType>0</confirmType>\n" +
                "    <status>FULFILLED</status>\n" +
                "    <operateTime>2016-09-01 10:26:07</operateTime>\n" +
                "  </entryOrder>\n" +
                "  <orderLines>\n" +
                "    <orderLine>\n" +
                "      <orderLineNo>1</orderLineNo>\n" +
                "      <itemCode>6928820014820</itemCode>\n" +
                "      <itemId>f5868b1795f4484c96ffb94192b7a595</itemId>\n" +
                "      <actualQty>6</actualQty>\n" +
                "    </orderLine>\n" +
                "    <orderLine>\n" +
                "      <orderLineNo>2</orderLineNo>\n" +
                "      <itemCode>6928820013960</itemCode>\n" +
                "      <itemId>b522e4ac3364481d99b51b2c5f1270aa</itemId>\n" +
                "      <actualQty>5</actualQty>\n" +
                "    </orderLine>\n" +
                "    <orderLine>\n" +
                "      <orderLineNo>3</orderLineNo>\n" +
                "      <itemCode>6903148127919</itemCode>\n" +
                "      <itemId>7ca341bf26064c4fb75c84f51f164138</itemId>\n" +
                "      <actualQty>10</actualQty>\n" +
                "    </orderLine>\n" +
                "  </orderLines>\n" +
                "</request>";
        try {


            String appscret = "sandboxfe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://127.0.0.1:8089/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception ex) {
        }
    }


    @Test
    public void CGTH() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "stockout.confirm");
        map.put("timestamp", "2016-09-01 14:16:17");
        map.put("format", "xml");
        map.put("app_key", "1023178000");
        map.put("v", "2.3");
        map.put("sign_method", "md5");
        map.put("customerId", "c1472610250306");
        String body = "<request>\n" +
                "  <deliveryOrder>\n" +
                "    <deliveryOrderCode>THD201607080000003</deliveryOrderCode>\n" +
                "    <deliveryOrderId>FH1607110001001</deliveryOrderId>\n" +
                "    <warehouseCode />\n" +
                "    <orderType>CGTH</orderType>\n" +
                "    <status>DELIVERED</status>\n" +
                "    <outBizCode />\n" +
                "    <confirmType>0</confirmType>\n" +
                "    <logisticsCode />\n" +
                "    <logisticsName />\n" +
                "    <expressCode />\n" +
                "    <orderConfirmTime>2016-07-11 9:30:44</orderConfirmTime>\n" +
                "  </deliveryOrder>\n" +
                "  <packages>\n" +
                "    <package>\n" +
                "      <logisticsName />\n" +
                "      <expressCode />\n" +
                "      <packageCode />\n" +
                "      <length>0.00</length>\n" +
                "      <width>0.00</width>\n" +
                "      <height>0.00</height>\n" +
                "      <weight>0.00</weight>\n" +
                "      <volume>0.000</volume>\n" +
                "      <packageMaterialList>\n" +
                "        <packageMaterial>\n" +
                "          <type />\n" +
                "          <quantity>0</quantity>\n" +
                "        </packageMaterial>\n" +
                "      </packageMaterialList>\n" +
                "      <items>\n" +
                "        <item>\n" +
                "          <itemCode>C546456</itemCode>\n" +
                "          <itemId />\n" +
                "          <quantity>22</quantity>\n" +
                "        </item>\n" +
                "        <item>\n" +
                "          <itemCode>C456456</itemCode>\n" +
                "          <itemId />\n" +
                "          <quantity>22</quantity>\n" +
                "        </item>\n" +
                "      </items>\n" +
                "    </package>\n" +
                "  </packages>\n" +
                "  <orderLines>\n" +
                "    <orderLine>\n" +
                "      <outBizCode />\n" +
                "      <orderLineNo />\n" +
                "      <itemCode>C546456</itemCode>\n" +
                "      <itemId />\n" +
                "      <itemName />\n" +
                "      <inventoryType />\n" +
                "      <actualQty>22</actualQty>\n" +
                "      <batchCode />\n" +
                "      <productDate />\n" +
                "      <expireDate />\n" +
                "      <produceCode />\n" +
                "      <batchs>\n" +
                "        <batch>\n" +
                "          <batchCode />\n" +
                "          <productDate />\n" +
                "          <expireDate />\n" +
                "          <produceCode />\n" +
                "          <inventoryType />\n" +
                "          <actualQty />\n" +
                "        </batch>\n" +
                "      </batchs>\n" +
                "    </orderLine>\n" +
                "    <orderLine>\n" +
                "      <outBizCode />\n" +
                "      <orderLineNo />\n" +
                "      <itemCode>C456456</itemCode>\n" +
                "      <itemId />\n" +
                "      <itemName />\n" +
                "      <inventoryType />\n" +
                "      <actualQty>22</actualQty>\n" +
                "      <batchCode />\n" +
                "      <productDate />\n" +
                "      <expireDate />\n" +
                "      <produceCode />\n" +
                "      <batchs>\n" +
                "        <batch>\n" +
                "          <batchCode />\n" +
                "          <productDate />\n" +
                "          <expireDate />\n" +
                "          <produceCode />\n" +
                "          <inventoryType />\n" +
                "          <actualQty />\n" +
                "        </batch>\n" +
                "      </batchs>\n" +
                "    </orderLine>\n" +
                "  </orderLines>\n" +
                "</request> ";
        try {


            String appscret = "sandboxfe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://127.0.0.1:8089/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception ex) {
        }
    }


    @Test
    public void SCRT() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "stockchange.report");
        map.put("timestamp", "2017-08-14 14:16:17");
        map.put("format", "xml");
        map.put("app_key", "1023012571");
        map.put("v", "2.3");
        map.put("sign_method", "md5");
        map.put("customerId", "c1499761264675");
        String body = "<request xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \n" +
                "\n" +
                "\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema \n" +
                "\n" +
                "\">\n" +
                "  <items>\n" +
                "    <item>\n" +
                "      <ownerCode>TEST</ownerCode>\n" +
                "      <warehouseCode>TEST002</warehouseCode>\n" +
                "      <orderCode>DO201707170000001</orderCode>\n" +
                "      <outBizCode>1eb20987-b040-470a-a0ca-5b6ef649e4d6</outBizCode>\n" +
                "      <itemCode>712test-03</itemCode>\n" +
                "      <quantity>3</quantity>\n" +
                "    </item>\n" +
                "  </items>\n" +
                "  <extendProps>{\"warehouseCode\":\"TEST002\",\"outBizCode\":\"7361b484-727d-4640-b9e7-a3e8c23ede3b\",\"InventoryTime\":\"2017-08-14T15:44:50.8719649+08:00\",\"InventoryInfoList\":[{\"GoodsCode\":\"712test-02\",\"GoodsNum\":4.0,\"InventoryType\":\"ZP\",\"Remark\":null},{\"GoodsCode\":\"712test-01\",\"GoodsNum\":5.0,\"InventoryType\":\"ZP\",\"Remark\":null},{\"GoodsCode\":\"712test-03\",\"GoodsNum\":3.0,\"InventoryType\":\"ZP\",\"Remark\":null}]}</extendProps>\n" +
                "</request>";
        try {


            String appscret = "2921843fe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://127.0.0.1:8084/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception ex) {
        }
    }

    @Test
    public void testheard12() {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("method", "entryorder.confirm");
            map.put("timestamp", "2016-12-06 14:09:00");
            map.put("format", "xml");
            map.put("app_key", "23067997");
            map.put("v", "2.0");
            map.put("sign_method", "md5");
            map.put("customerId", "2148150915");
            String body = "<request><entryOrder><warehouseCode>CAN206</warehouseCode><entryOrderCode>CGDD201612010000002</entryOrderCode><entryOrderId>LBX022915632557696</entryOrderId><entryOrderType>CGRK</entryOrderType><outBizCode>STHP161201000010_0</outBizCode><confirmType>0</confirmType><status>FULFILLED</status></entryOrder><orderLines><orderLine><itemCode>6932548665899</itemCode><itemId>540928331183</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548665905</itemCode><itemId>540927226280</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548665912</itemCode><itemId>540927298084</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548695483</itemCode><itemId>539946598799</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548695490</itemCode><itemId>539946802312</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548695513</itemCode><itemId>539945776825</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548695445</itemCode><itemId>539945844773</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548695452</itemCode><itemId>539946084386</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548695469</itemCode><itemId>539946733240</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548666858</itemCode><itemId>534830862073</itemId><inventoryType>ZP</inventoryType><actualQty>13</actualQty></orderLine><orderLine><itemCode>6932548666834</itemCode><itemId>534898080840</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548666841</itemCode><itemId>534898468129</itemId><inventoryType>ZP</inventoryType><actualQty>81</actualQty></orderLine><orderLine><itemCode>6932548666865</itemCode><itemId>534764275977</itemId><inventoryType>ZP</inventoryType><actualQty>40</actualQty></orderLine><orderLine><itemCode>6932548694714</itemCode><itemId>539557736565</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548694721</itemCode><itemId>539558617628</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548667282</itemCode><itemId>534864308708</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548667299</itemCode><itemId>534796198829</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548667305</itemCode><itemId>534830377147</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548667275</itemCode><itemId>534830065773</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548667473</itemCode><itemId>534796714229</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548667480</itemCode><itemId>534796698209</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548667497</itemCode><itemId>534863948745</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548603228</itemCode><itemId>534782311410</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548603235</itemCode><itemId>534781915849</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548603242</itemCode><itemId>534848610933</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548603259</itemCode><itemId>534848554697</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548666049</itemCode><itemId>542608893644</itemId><inventoryType>ZP</inventoryType><actualQty>45</actualQty></orderLine><orderLine><itemCode>6932548666056</itemCode><itemId>542590844014</itemId><inventoryType>ZP</inventoryType><actualQty>60</actualQty></orderLine><orderLine><itemCode>6932548666063</itemCode><itemId>542590640587</itemId><inventoryType>ZP</inventoryType><actualQty>30</actualQty></orderLine><orderLine><itemCode>6932548695414</itemCode><itemId>539971803456</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548695421</itemCode><itemId>539970024646</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548695995</itemCode><itemId>539970789063</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548697456</itemCode><itemId>540211010951</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548697463</itemCode><itemId>540211322495</itemId><inventoryType>ZP</inventoryType><actualQty>17</actualQty></orderLine><orderLine><itemCode>6932548697470</itemCode><itemId>540211234837</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548697487</itemCode><itemId>540211214579</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548697494</itemCode><itemId>540211829294</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548697500</itemCode><itemId>540210923213</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548697562</itemCode><itemId>540211059017</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548697548</itemCode><itemId>540211493728</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548697555</itemCode><itemId>540211913124</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548698316</itemCode><itemId>540206552476</itemId><inventoryType>ZP</inventoryType><actualQty>8</actualQty></orderLine><orderLine><itemCode>6932548698323</itemCode><itemId>540210987157</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548698330</itemCode><itemId>540211585545</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548663031</itemCode><itemId>534861528483</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548663048</itemCode><itemId>534793702529</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548663055</itemCode><itemId>534793690826</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548663062</itemCode><itemId>534861680093</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548698095</itemCode><itemId>540211693606</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548698057</itemCode><itemId>540210775359</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548698064</itemCode><itemId>540211202944</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548601415</itemCode><itemId>534818989629</itemId><inventoryType>ZP</inventoryType><actualQty>74</actualQty></orderLine><orderLine><itemCode>6932548601422</itemCode><itemId>534880389936</itemId><inventoryType>ZP</inventoryType><actualQty>80</actualQty></orderLine><orderLine><itemCode>6932548668111</itemCode><itemId>534864492247</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548668128</itemCode><itemId>534864492256</itemId><inventoryType>ZP</inventoryType><actualQty>24</actualQty></orderLine><orderLine><itemCode>6932548668135</itemCode><itemId>534830373248</itemId><inventoryType>ZP</inventoryType><actualQty>30</actualQty></orderLine><orderLine><itemCode>6932548668142</itemCode><itemId>534796722693</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548667510</itemCode><itemId>534731983954</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548667527</itemCode><itemId>534864829230</itemId><inventoryType>ZP</inventoryType><actualQty>20</actualQty></orderLine><orderLine><itemCode>6932548667534</itemCode><itemId>534831793749</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548667541</itemCode><itemId>534765275936</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548667725</itemCode><itemId>534864793079</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548667732</itemCode><itemId>534796498428</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548667749</itemCode><itemId>534830273325</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548601439</itemCode><itemId>534719387229</itemId><inventoryType>ZP</inventoryType><actualQty>29</actualQty></orderLine><orderLine><itemCode>6932548668241</itemCode><itemId>534829814192</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548602009</itemCode><itemId>534785190851</itemId><inventoryType>ZP</inventoryType><actualQty>17</actualQty></orderLine><orderLine><itemCode>6932548602016</itemCode><itemId>534848206931</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548602023</itemCode><itemId>534853172672</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548665844</itemCode><itemId>540921005832</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548665851</itemCode><itemId>540927238234</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548665868</itemCode><itemId>540921009826</itemId><inventoryType>ZP</inventoryType><actualQty>5</actualQty></orderLine><orderLine><itemCode>6932548665875</itemCode><itemId>540921277095</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548665769</itemCode><itemId>540912780389</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548665776</itemCode><itemId>540927270165</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548665783</itemCode><itemId>540928371161</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548665806</itemCode><itemId>540921177376</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548665813</itemCode><itemId>540927270163</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548665820</itemCode><itemId>540928427017</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548699962</itemCode><itemId>541192182794</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548699979</itemCode><itemId>541192170789</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548699986</itemCode><itemId>541185097333</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548600678</itemCode><itemId>534847470810</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548600685</itemCode><itemId>534781287824</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548695193</itemCode><itemId>539918228779</itemId><inventoryType>ZP</inventoryType><actualQty>20</actualQty></orderLine><orderLine><itemCode>6932548603112</itemCode><itemId>534916268618</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548603129</itemCode><itemId>534883669559</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548603136</itemCode><itemId>534782303653</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548669859</itemCode><itemId>534765163912</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548669866</itemCode><itemId>534831298912</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548671463</itemCode><itemId>534833498123</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548668326</itemCode><itemId>534897524150</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548600722</itemCode><itemId>534918444801</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548622052</itemCode><itemId>534894204367</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548622069</itemCode><itemId>534859505063</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548698149</itemCode><itemId>540209064206</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548668364</itemCode><itemId>534831602638</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548668371</itemCode><itemId>534765875252</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548663130</itemCode><itemId>534727435536</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548663154</itemCode><itemId>534793798497</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548602214</itemCode><itemId>534915796541</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548693953</itemCode><itemId>538715541451</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548693960</itemCode><itemId>538715597381</itemId><inventoryType>ZP</inventoryType><actualQty>0</actualQty></orderLine><orderLine><itemCode>6932548692345</itemCode><itemId>537488093355</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548692352</itemCode><itemId>537480137143</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548666827</itemCode><itemId>534829706135</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548606144</itemCode><itemId>534880829582</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548606151</itemCode><itemId>534881069400</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548606328</itemCode><itemId>534881113395</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548606335</itemCode><itemId>534781915819</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548603051</itemCode><itemId>534916572148</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548600814</itemCode><itemId>534847802700</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548600821</itemCode><itemId>534847774768</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548600838</itemCode><itemId>534918904267</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548692796</itemCode><itemId>537372851342</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548692802</itemCode><itemId>537372579790</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548620171</itemCode><itemId>534750763326</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548696381</itemCode><itemId>540210863287</itemId><inventoryType>ZP</inventoryType><actualQty>2</actualQty></orderLine><orderLine><itemCode>6932548696404</itemCode><itemId>540211050868</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548601507</itemCode><itemId>534880697482</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548601514</itemCode><itemId>534848482544</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548601521</itemCode><itemId>534781807940</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548602818</itemCode><itemId>534848374700</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548662522</itemCode><itemId>534861352487</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548662539</itemCode><itemId>534827693096</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548662546</itemCode><itemId>534864248468</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548695964</itemCode><itemId>539971715675</itemId><inventoryType>ZP</inventoryType><actualQty>19</actualQty></orderLine><orderLine><itemCode>6932548695971</itemCode><itemId>539970541644</itemId><inventoryType>ZP</inventoryType><actualQty>20</actualQty></orderLine><orderLine><itemCode>6932548695988</itemCode><itemId>539971651818</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548695407</itemCode><itemId>539971419962</itemId><inventoryType>ZP</inventoryType><actualQty>1</actualQty></orderLine><orderLine><itemCode>6932548696091</itemCode><itemId>539946566896</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548696114</itemCode><itemId>539946849095</itemId><inventoryType>ZP</inventoryType><actualQty>15</actualQty></orderLine><orderLine><itemCode>6932548695070</itemCode><itemId>539642804730</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548695087</itemCode><itemId>539642547823</itemId><inventoryType>ZP</inventoryType><actualQty>24</actualQty></orderLine><orderLine><itemCode>6932548695094</itemCode><itemId>539644021227</itemId><inventoryType>ZP</inventoryType><actualQty>14</actualQty></orderLine><orderLine><itemCode>6932548695100</itemCode><itemId>539642815145</itemId><inventoryType>ZP</inventoryType><actualQty>9</actualQty></orderLine><orderLine><itemCode>MZ1407723M</itemCode><itemId>542496903460</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548615511</itemCode><itemId>542451044810</itemId><inventoryType>ZP</inventoryType><actualQty>4</actualQty></orderLine><orderLine><itemCode>6932548615528</itemCode><itemId>542450988958</itemId><inventoryType>ZP</inventoryType><actualQty>9</actualQty></orderLine><orderLine><itemCode>6932548615535</itemCode><itemId>542485095520</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>6932548615559</itemCode><itemId>542468737008</itemId><inventoryType>ZP</inventoryType><actualQty>20</actualQty></orderLine><orderLine><itemCode>6932548615566</itemCode><itemId>542451396216</itemId><inventoryType>ZP</inventoryType><actualQty>21</actualQty></orderLine><orderLine><itemCode>6932548615573</itemCode><itemId>542477130869</itemId><inventoryType>ZP</inventoryType><actualQty>12</actualQty></orderLine><orderLine><itemCode>6932548615580</itemCode><itemId>542451148664</itemId><inventoryType>ZP</inventoryType><actualQty>16</actualQty></orderLine><orderLine><itemCode>6932548685309</itemCode><itemId>534925759441</itemId><inventoryType>ZP</inventoryType><actualQty>33</actualQty></orderLine><orderLine><itemCode>6932548685279</itemCode><itemId>535024881848</itemId><inventoryType>ZP</inventoryType><actualQty>54</actualQty></orderLine><orderLine><itemCode>6932548685286</itemCode><itemId>534993850014</itemId><inventoryType>ZP</inventoryType><actualQty>55</actualQty></orderLine><orderLine><itemCode>6932548685293</itemCode><itemId>535025289164</itemId><inventoryType>ZP</inventoryType><actualQty>29</actualQty></orderLine><orderLine><itemCode>MZ0806102S</itemCode><itemId>540119578590</itemId><inventoryType>ZP</inventoryType><actualQty>7</actualQty></orderLine><orderLine><itemCode>MZ0806102M</itemCode><itemId>540119337727</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>MZ0806102L</itemCode><itemId>540119819028</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>MZ0806102XL</itemCode><itemId>540119335802</itemId><inventoryType>ZP</inventoryType><actualQty>0</actualQty></orderLine><orderLine><itemCode>6932548665592</itemCode><itemId>541166848003</itemId><inventoryType>ZP</inventoryType><actualQty>27</actualQty></orderLine><orderLine><itemCode>6932548665608</itemCode><itemId>541186199202</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548665615</itemCode><itemId>541186123477</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548665622</itemCode><itemId>541176241407</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548666070</itemCode><itemId>542616182653</itemId><inventoryType>ZP</inventoryType><actualQty>18</actualQty></orderLine><orderLine><itemCode>6932548607264</itemCode><itemId>534785846087</itemId><inventoryType>ZP</inventoryType><actualQty>8</actualQty></orderLine><orderLine><itemCode>6932548607271</itemCode><itemId>534785762198</itemId><inventoryType>ZP</inventoryType><actualQty>94</actualQty></orderLine><orderLine><itemCode>6932548607288</itemCode><itemId>534819289383</itemId><inventoryType>ZP</inventoryType><actualQty>80</actualQty></orderLine><orderLine><itemCode>6932548607295</itemCode><itemId>534853324470</itemId><inventoryType>ZP</inventoryType><actualQty>26</actualQty></orderLine><orderLine><itemCode>6932548688034</itemCode><itemId>542538546549</itemId><inventoryType>ZP</inventoryType><actualQty>11</actualQty></orderLine><orderLine><itemCode>6932548688041</itemCode><itemId>542547299653</itemId><inventoryType>ZP</inventoryType><actualQty>34</actualQty></orderLine><orderLine><itemCode>6932548688058</itemCode><itemId>542530253981</itemId><inventoryType>ZP</inventoryType><actualQty>6</actualQty></orderLine><orderLine><itemCode>6932548688065</itemCode><itemId>542538766025</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548601477</itemCode><itemId>534848214681</itemId><inventoryType>ZP</inventoryType><actualQty>151</actualQty></orderLine><orderLine><itemCode>6932548601484</itemCode><itemId>534848310615</itemId><inventoryType>ZP</inventoryType><actualQty>66</actualQty></orderLine><orderLine><itemCode>6932548601491</itemCode><itemId>534916164196</itemId><inventoryType>ZP</inventoryType><actualQty>32</actualQty></orderLine><orderLine><itemCode>6932548665929</itemCode><itemId>541166848289</itemId><inventoryType>ZP</inventoryType><actualQty>27</actualQty></orderLine><orderLine><itemCode>6932548665936</itemCode><itemId>541186159538</itemId><inventoryType>ZP</inventoryType><actualQty>31</actualQty></orderLine><orderLine><itemCode>6932548665943</itemCode><itemId>541183446310</itemId><inventoryType>ZP</inventoryType><actualQty>14</actualQty></orderLine><orderLine><itemCode>6932548665950</itemCode><itemId>541183470248</itemId><inventoryType>ZP</inventoryType><actualQty>10</actualQty></orderLine><orderLine><itemCode>6932548695285</itemCode><itemId>539919134308</itemId><inventoryType>ZP</inventoryType><actualQty>3</actualQty></orderLine><orderLine><itemCode>6932548695292</itemCode><itemId>539918257722</itemId><inventoryType>ZP</inventoryType><actualQty>12</actualQty></orderLine><orderLine><itemCode>6932548695308</itemCode><itemId>539919174295</itemId><inventoryType>ZP</inventoryType><actualQty>13</actualQty></orderLine><orderLine><itemCode>6932548695315</itemCode><itemId>539919103466</itemId><inventoryType>ZP</inventoryType><actualQty>9</actualQty></orderLine><orderLine><itemCode>6932548696084</itemCode><itemId>539947331055</itemId><inventoryType>ZP</inventoryType><actualQty>80</actualQty></orderLine></orderLines></request>";
            String appscret = "2921843fe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://121.199.168.185:30007/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }


    @Test
    public void stockoutconfirm() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "stockout.confirm");
        map.put("timestamp", "2016-09-01 14:16:17");
        map.put("format", "xml");
        map.put("app_key", "23067997");
        map.put("v", "2.0");
        map.put("sign_method", "md5");
        map.put("customerId", "mlbe");
        String body = "<request><deliveryOrder><totalOrderLines>2</totalOrderLines><deliveryOrderCode>DBD201711170000001</deliveryOrderCode><deliveryOrderId>Z310000641391</deliveryOrderId><warehouseCode>B01</warehouseCode><orderType>DBCK</orderType><status>DELIVERED</status><outBizCode>Z310000641391</outBizCode><confirmType>0</confirmType><logisticsCode>DISTRIBUTOR_13323734</logisticsCode><logisticsName>JY</logisticsName><expressCode>JY0004059205</expressCode><orderConfirmTime>2017-11-17 14:24:01</orderConfirmTime></deliveryOrder><packages><package><logisticsName>JY</logisticsName><expressCode>JY0004059205</expressCode><packageCode></packageCode><length>0.0</length><width>0.0</width><height>0.0</height><weight>0.0</weight><volume>0.0</volume><packageMaterialList/><items><item><itemCode>DL01</itemCode><itemId>1700030930</itemId><quantity>500</quantity></item><item><itemCode>YHT02</itemCode><itemId>1700052029</itemId><quantity>480</quantity></item></items></package></packages><orderLines><orderLine><outBizCode>Z31000064139111</outBizCode><orderLineNo></orderLineNo><itemCode>DL01</itemCode><itemId>1700030930</itemId><itemName></itemName><inventoryType>ZP</inventoryType><actualQty>500</actualQty><batchCode></batchCode><productDate></productDate><expireDate></expireDate><produceCode></produceCode><batchs/></orderLine><orderLine><outBizCode>Z31000064139122</outBizCode><orderLineNo></orderLineNo><itemCode>YHT02</itemCode><itemId>1700052029</itemId><itemName></itemName><inventoryType>ZP</inventoryType><actualQty>480</actualQty><batchCode></batchCode><productDate></productDate><expireDate></expireDate><produceCode></produceCode><batchs/></orderLine></orderLines></request>";
        try {
            String appscret = "2921843fe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://127.0.0.1:8084/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception ex) {
        }
    }

    @Test
    public void transferorderreport() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "transferorder.report");
        map.put("timestamp", "2016-09-01 14:16:17");
        map.put("format", "xml");
        map.put("app_key", "23067997");
        map.put("v", "2.0");
        map.put("sign_method", "md5");
        map.put("customerId", "mlbe");
        String body = "<request><transferOrderCode>IBC34j3j3k</transferOrderCode><erpOrderCode>XXX</erpOrderCode><ownerCode>3654657878</ownerCode><orderStatus>10</orderStatus><transferOutOrderCode>LBX112251140388447</transferOutOrderCode><transferInOrderCode>LBX102251140388448</transferInOrderCode><createTime>2017-10-15 10:10:10</createTime><fromWarehouseCode>123456</fromWarehouseCode><toWarehouseCode>123456</toWarehouseCode><confirmOutTime>2017-10-15 10:10:10</confirmOutTime><confirmInTime>2017-10-15 10:10:10</confirmInTime><items><item><scItemCode>123456</scItemCode><inventoryType>1</inventoryType><planCount>2</planCount><outCount>2</outCount><inCount>2</inCount></item></items></request>";
        try {
            String appscret = "2921843fe5ec500237d2851cf6049c98";
            String sign = SropUtils.sign(map, body, appscret);
            map.put("sign", sign);
            String url = "http://127.0.0.1:8084/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
            String str = HttpHelper.doPost(url, body);
            System.out.print(str);
        } catch (Exception ex) {
        }
    }




}
