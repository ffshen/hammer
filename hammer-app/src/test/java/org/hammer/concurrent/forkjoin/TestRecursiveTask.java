package org.hammer.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

import org.hammer.vo.TestResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenx
 * @date 2017年1月15日
 * @see
 * @version
 */
public class TestRecursiveTask  extends RecursiveTask< List<TestResultVO> > {      

    private static final Logger log = LoggerFactory.getLogger(TestRecursiveTask.class);    
     
    private static final long serialVersionUID = -6046963496469041354L;
    
    private List<String> datas ;

    public TestRecursiveTask(List<String> datas){
        this.datas = datas ;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.RecursiveTask#compute()
     */
    @Override
    protected  List<TestResultVO> compute() {
        if(Objects.equals(getDatas().size(), 1) ){                     
            List<TestResultVO> list = new ArrayList<>() ;
            list.add(new TestResultVO(new Random().nextInt(100) ,getDatas().get(0))) ;
            return list ;
        }
        else{
            log.info("getDatas().subList(0, 1):{}",getDatas().subList(0, 1));
            log.info("getDatas().subList(1, getDatas().size()-1):{}",getDatas().subList(1, getDatas().size()));
            TestRecursiveTask  task1 =  new TestRecursiveTask (getDatas().subList(0, 1)) ;
            TestRecursiveTask  task2 =  new TestRecursiveTask (getDatas().subList(1, getDatas().size())) ;
            task1.fork();
            task2.fork();         
            List<TestResultVO> list = new ArrayList<>() ; 
            list.addAll(task1.join()) ;
            list.addAll(task2.join()) ;
            return list ;
        }
    }

    
}
