package model.filtererr;

import model.filtererr.Monitor.FileMonitor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FiltererrApplication {

    @Value("${listening_path}")
    private String[] listening_path;

    public static void main(String[] args) {
        SpringApplication.run(FiltererrApplication.class,args);
    }

    @Bean
    public FileMonitor fileMonitor(){
        return new FileMonitor(listening_path);
    }

}
