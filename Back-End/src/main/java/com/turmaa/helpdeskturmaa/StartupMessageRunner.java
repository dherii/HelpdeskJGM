package com.turmaa.helpdeskturmaa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessageRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n\n============================================================");
        System.out.println("=                                                            =");
        System.out.println("=          APLICAÇÃO HELPDESK INICIADA COM SUCESSO           =");
        System.out.println("=                                                            =");
        System.out.println("=  Acesse o H2 Console em: http://localhost:8080/h2-console  =");
        // System.out.println("=  Acesse a API em: http://localhost:8080/<seus-endpoints>   =");
        System.out.println("=                                                            =");
        System.out.println("============================================================\n");
    }
}
