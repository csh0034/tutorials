# Google Sheets

## Troubleshooting

### Service Account 사용시 403 이슈

서비스 어카운트를 발급받아 사용할 경우 사용하려는 구글 시트에 대해서  
발급받은 json 파일의 client_email 의 대상에게 읽기 공유를 해야함.

## 참조

- [Google Sheets Java API](https://developers.google.com/sheets/api/quickstart/java)
- [Google Cloud, 서비스 계정 키 생성 및 관리](https://cloud.google.com/iam/docs/creating-managing-service-account-keys?hl=ko#iam-service-account-keys-create-console)
- [How to read from and write into Google Sheets from your robots](https://robocorp.com/docs/development-guide/google-sheets/interacting-with-google-sheets)
