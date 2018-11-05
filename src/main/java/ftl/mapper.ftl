package ${mapper.javaPackage};

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.spc.count.bean.${mapper.entityName};

@Repository
public interface ${mapper.className} extends MongoRepository<${mapper.entityName},String>{
  
}  