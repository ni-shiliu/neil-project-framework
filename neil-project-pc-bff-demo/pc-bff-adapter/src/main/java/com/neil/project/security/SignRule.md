### 参数加签规则：
- 1、参数按名称排序，并用&相连；  

例：id=我是ID&orderNo=我是订单号

- 2、参数末尾拼接nonce和timestamp

例：id=我是ID&orderNo=我是订单号&nonce=ee1af86c-7c1e-452d-9b49-c40f6cc7e36b&timestamp=1724744519301

3、用HmacSHA256加签

```java
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String secret = "1234567890";
        String queryString = "id=我是ID&orderNo=我是订单号&nonce=ee1af86c-7c1e-452d-9b49-c40f6cc7e36b&timestamp=1724744519301";
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(queryString.getBytes());
        String sign = Base64.getEncoder().encodeToString(hmacBytes);
        System.out.println(sign);
    }

```
