## 테스트 디바이스 Hash Id 구하는 법
https://developers.google.com/admob/android/test-ads?hl=ko#add_your_test_device_programmatically

프로젝트를 받은 후 앱을 빌드하여 "Click! GDPR" 버튼을 누른후 로그를 보면 

Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("device-hash-id") to set this as a debug device.

라는 로그가 있다. "device-hash-id" 값을 쓰면 된다.

## admob app id 구하는 법
https://support.google.com/admob/answer/7356431?hl=ko

## reference
https://github.com/googleads/googleads-consent-sdk-android/tree/main/consent-library/src/main/java/com/google/ads/consent
