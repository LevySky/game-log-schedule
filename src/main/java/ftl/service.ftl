package ${service.javaPackage};

import com.spc.common.Constant;
import com.spc.schedule.manager.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.spc.count.BaseCountMananger;


import com.spc.count.bean.${service.entityName};
import com.spc.count.repository.${service.mapperName};

@Component
public class ${service.entityName}Manager extends BaseCountMananger{

    public static ${service.entityName}Manager manager;

    @Qualifier("loggerMongo")
    @Autowired
    MongoTemplate loggerMongoTemplate;

    @Qualifier("mongo")
    @Autowired
    MongoTemplate countMongoTemplate;


    @Autowired
    ${service.entityName}Repository repository;


    public static HandleResult etl(Date date,List<Integer> serverIdList) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String countDayStr = sdf.format(Constant.getLastDay(date)) ;
    try {
    TypedAggregation<${service.entityName}> agg = Aggregation.newAggregation(
     ${service.entityName}.class
        , Aggregation.match(getCriteria(date,serverIdList))
        ,Aggregation.group("serverId","playerId").max("newLevel").as("level").max("logDate").as("logDate")
        );

        //System.out.println(agg);
        agg.withOptions(AggregationOptions.builder().allowDiskUse(true).build());
        AggregationResults<${service.entityName}> aggRsult = manager.loggerMongoTemplate.aggregate(agg, "xxx", ${service.entityName}.class);
            if(aggRsult.getMappedResults() != null || !aggRsult.getMappedResults().isEmpty()){
            aggRsult.getMappedResults().forEach(level->{
            level.setLogDate(level.getLogDate());
            });
            manager.repository.saveAll(aggRsult.getMappedResults());
            }
            return  HandleResult.of(true,countDayStr+" 共统计："+aggRsult.getMappedResults().size());
            } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false,countDayStr+"  "+ e.getLocalizedMessage());
          }
      }

    public static long delete(Date date, List<Integer> serverIdList){
      return delete(date,serverIdList,manager.countMongoTemplate,"c_");
    }

    @PostConstruct
    public void init() {
        manager = this;
    }
}  