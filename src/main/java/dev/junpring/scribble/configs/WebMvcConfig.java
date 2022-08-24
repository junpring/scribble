package dev.junpring.scribble.configs;

import dev.junpring.scribble.interceptors.SessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * WebMvcConfigurer
     * - 내부에는 많은 메소드들이 존재하는데 모두 default로 선언되어 있습니다. 즉, implements를 하여도 전체를 @Override할 필요 없이 필요한 부분만 설정할 수 있도록 되어 있는 구조입니다.
     */
    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }
    /**
     * - 스프링 컨테이너는 @Configuration이 붙어있는 클래스를 자동으로 빈으로 등록해두고,
     * 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈을 생성해준다.
     * - @Bean을 사용해 수동으로 빈을 등록해줄 때에는 메소드 이름으로 빈 이름이 결정된다.
     **/
    /**
     * 이러한 @Bean 어노테이션의 경우는 수동으로 빈을 직접 등록해줘야만 하는 상황인데, 주로 다음과 같을 때 사용한다.
     * <p>
     * 1. 개발자가 직접 제어가 불가능한 라이브러리를 활용할 때
     * 2. 애플리케이션 전범위적으로 사용되는 클래스를 등록할 때
     * 3. 다형성을 활용하여 여러 구현체를 등록해주어야 할 때
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.sessionInterceptor()) // 핸들러 지정.
                .addPathPatterns("/**") // 인터셉트할 기본 패턴을 지정
                .excludePathPatterns("/resources/**");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
        // user/login  (o)
        // user/login/ (x) 상대경로 설정
    }
}
