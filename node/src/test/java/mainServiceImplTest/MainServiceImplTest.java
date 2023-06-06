package mainServiceImplTest;

import org.amaltsev.NodeApplication;
import org.amaltsev.dao.RawDataDAO;
import org.amaltsev.entity.RawData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes={NodeApplication.class})
class MainServiceImplTest {

    @Autowired
    private RawDataDAO rawDataDAO;

    @Test
    public void testRawDataDto() {
        //конфигурация апдейта
        Update update = new Update();
        Message message = new Message();
        message.setText("aaavaaa");
        update.setMessage(message);
//        Объект роудата с подготовленным апдейтом
        RawData rawData = RawData.builder().event(update).build();
        Set<RawData> testData = new HashSet<>();
//        сохранить в хэшколлекцию
        testData.add(rawData);
//        Сохранитьв базу данных (судб сама проставит айдишник )
        rawDataDAO.save(rawData);
//
        Assert.isTrue(testData.contains(rawData), "entity not found in the set");
//
    }
}
