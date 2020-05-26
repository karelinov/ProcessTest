package com.sample;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

import com.sample.processcontext.DataContext;

/**
 * This is a sample file to launch a process.
 */
public class ProcessTest {

    public static final void main(String[] args) {
    	KieRuntimeLogger kieLogger = null;
    	
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-process");
        	//kieLogger = ks.getLoggers().newConsoleLogger(kSession);

            // Создание параметров процесса
        	Map<String,Object> blockFunctionParams = new HashMap<String,Object>();
        	//blockFunctionParams.put("context", new BlockFunctionContext());
        	
        	// Создаём блоки функций
        	DataContext datacontext = new DataContext();
        	datacontext.defSet("RESULT", 	"EVAL{[],X1}");
        	datacontext.defSet("X1",		"EXISTS{[K1],X2}");
        	datacontext.defSet("K1",		"SELECT{[],FAMILY1}");
        	datacontext.defSet("FAMILY1",	"RULE{[],1001_FAMILY}");
        	datacontext.defSet("X2", 		"EVAL{[],X4*(X5+X6)}");
        	datacontext.defSet("X4", 		"RULE{[],1001_HAS_1002}");
        	datacontext.defSet("X5", 		"RULE{[],1001_AGE_LT_16}");
        	datacontext.defSet("X6", 		"RULE{[],1001_AGE_16_18_EDU}");
        	datacontext.put("RESULTDEFINITION", "RESULT"); // задаём корневой блок функций для старта
        	
        	datacontext.context.coAdd("CHECK_CITIZEN_PK", (Integer)1);

            blockFunctionParams.put("datacontext", datacontext);
        	
            // запуск процесса
        	ProcessInstance blockFunction = kSession.createProcessInstance("com.sample.bpmn.BlockFunction", blockFunctionParams);
        	kSession.insert(blockFunction);
        	kSession.startProcessInstance(blockFunction.getId());
        	InsertData.Insert1(kSession);
        	kSession.fireAllRules();
        	if (kieLogger != null) kieLogger.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
