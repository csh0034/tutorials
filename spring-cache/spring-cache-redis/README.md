# spring cache(redis)

## Troubleshooting

### spring boot 2.6.x 순환참조 금지

[Circular References Prohibited by Default](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.6-Release-Notes#circular-references-prohibited-by-default) 순환참조 금지 옵션이 기본으로 변경됨.  
따라서 Self Injection 을 처리할때 @Lazy 어노테이션을 같이 추가해야한다.

## 참조

- [circular-dependencies-in-spring](https://www.baeldung.com/circular-dependencies-in-spring)
