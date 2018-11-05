package com.spc.schedule.storage.ctr;

import com.spc.schedule.storage.repo.KafkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class KafkaController {


    @Autowired
    private KafkaRepository kafkaRepository;


    @PostMapping("/spc/kafka/getAll")
    @ResponseBody
    public Map kafka(@RequestBody Map map){
//        Integer pageNum = Integer.parseInt(map.get("pageNum").toString());
//        Integer pageSize = Integer.parseInt(map.get("pageSize").toString());
//        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
//
//        DBObject obj = new BasicDBObject();
//        if(map.get("topic") != null){
//            String topic = map.get("topic").toString();
//            if(StringUtils.isNotEmpty(topic)){
//                obj.put("topic", topic);
//            }
//
//        }
//        if(map.get("partition") != null){
//            String partition = map.get("partition").toString();
//            if(StringUtils.isNotEmpty(partition)){
//                obj.put("partition", Integer.parseInt(partition));
//            }
//
//        }
//        Query query = new BasicQuery(JSONObject.toJSONString(obj));
//        query.skip((pageNum - 1) * pageSize);
//        query.limit(pageSize);
//        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time")));
//
//       // System.out.println(query);
//        List<KafkaOffset> list = this.mongoTemplate.find(query,KafkaOffset.class);
//        Long count = this.mongoTemplate.count(query, KafkaOffset.class);
//
//        Map _map = new HashMap();
//        _map.put("content",list);
//        _map.put("totalPages",(int)Math.ceil(count/pageSize*1.0));
//        _map.put("totalElements",count);
        return null;
    }
}
