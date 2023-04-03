# Spring PDF

## [itextpdf](https://github.com/itext/itextpdf)

- AGPL License
- AGPL 라이선스는 AGPL 라이선스를 사용하는 프로젝트를 사용하는 경우 AGPL 라이선스를  
  사용하는 프로젝트의 소스코드를 공개해야한다.
- itext5 기반으로 EOL 이며 itext7 을 사용 권장

## [OpenPDF](https://github.com/LibrePDF/OpenPDF)

- LGPL License
- iText 의 LGPL/MPL 오픈 소스 후속 제품이며 itext4 svn 태그의 일부 포크를 기반으로 한다.
- html to pdf 를 직접 지원하지 않는다. flyingsaucer 를 사용해야함

## [flyingsaucer](https://github.com/flyingsaucerproject/flyingsaucer)

- LGPL License
- html to pdf 지원
- org.xhtmlrenderer:flying-saucer-pdf-openpdf

## [Apache PDFBox](https://github.com/apache/pdfbox)

- Apache License v2.0.
- html to pdf 를 직접 지원하지 않는다. openhtmltopdf 를 사용해야함

## [openhtmltopdf](https://github.com/danfickle/openhtmltopdf)

- LGPL License
- html to pdf 지원
- PDFBOX 기반으로 작성됨

### html > pdf 변환시 이슈

- 기본적으로 A4 사이즈(210mm X 297mm) 이며 96dpi 로 설정되어있다.
- 96dpi 기준 A4 사이즈를 px 로 변환하면 794px X 1123px 이다.

## 참조

- [baeldung, Creating PDF Files in Java](https://www.baeldung.com/java-pdf-creation)
- [baeldung, HTML to PDF Using OpenPDF](https://www.baeldung.com/java-html-to-pdf)
- [오픈소스 라이선스 변화의 흐름](https://tech.kakao.com/2021/09/08/opensource-license/)
