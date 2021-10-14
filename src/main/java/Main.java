import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    //маппер для преобразования json в объекты Fact
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        //клиент
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        //запрос Get
        HttpGet request = new HttpGet(
                "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        try {
            //отправляем запрос на сервер
            CloseableHttpResponse response = httpClient.execute(request);

            //преобразуем json из боди реквеста в коллекцию объектов Fact
            List<Fact> facts = mapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<List<Fact>>() {}
            );

            //Посмотрим сколько в списке получилось объектов
            System.out.println("Всего зачитано объектов из json: " + facts.size());

            //выведем как указано в задании только те за которые проголосовали хотя бы один раз
            System.out.println("Факты за которые проголосовали хотя бы один раз: ");
            facts.stream()
                    .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                    .forEach(value -> System.out.println(value.toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
