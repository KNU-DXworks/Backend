package project.DxWorks.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthereumConfig {

    @Bean
    public Web3j web3j() {
        // Ganache local node
        String rpcUrl = "http://127.0.0.1:8545";

        return Web3j.build(new HttpService(rpcUrl));
    }


}
