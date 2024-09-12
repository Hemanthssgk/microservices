package com.eazybytes.accounts.accountsFunctions;

import com.eazybytes.accounts.service.IAccountsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class FunctionConfigs {
    @Bean
    public Consumer<Long> updateCommunication(IAccountsService iAccountsService)
    {
        return message -> {
            boolean status = iAccountsService.updateCommunication(message);
            if (status)
            {
                System.out.println("Communication status - Updated inside database");
            }
            else
            {
                System.out.println("Communication failed - didn't update inside database");
            }
        };
    }
}
