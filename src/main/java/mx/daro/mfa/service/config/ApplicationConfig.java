package mx.daro.mfa.service.config;

import javax.sql.*;
import org.springframework.jdbc.datasource.lookup.*;
import mx.daro.mfa.service.utils.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;

@Configuration
@ComponentScan(basePackages = { "mx.daro.mfa.service.business" })
public class ApplicationConfig {
	
    @Bean(name = { "dataSource" })
    public DataSource dataSource() {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        return dataSourceLookup.getDataSource(Constants.DATA_SOURCE_NAME);
    }
    
    @Bean
    public JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }
}
