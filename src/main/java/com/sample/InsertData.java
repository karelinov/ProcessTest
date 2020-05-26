package com.sample;

import java.time.Period;

import org.kie.api.runtime.KieSession;

import com.sample.data.AtomType;
import com.sample.data.Person;
import com.sample.data.PersonData;

/**
 * This is a sample class to launch a rule.
 */
public class InsertData {

    public static final void Insert1(KieSession kSession) {

            // Проверяемое ФЛ
        	Person person = new Person(1);
            kSession.insert(person);
            // Атом ЛК 1001
            PersonData personData = new PersonData(person.person_pk);
            personData.atom_type = AtomType.BENEFIT_CATEGORY;
            personData.atom_id = "1001";
            personData.atom_state = true;
            personData.atom_value = true;
            kSession.insert(personData);
            
            // Другие ФЛ семьи
            Person person2 = new Person(2);
            kSession.insert(person2);
            // Атом ЛК 1002
            PersonData personData2 = new PersonData(person2.person_pk);
            personData2.atom_type = AtomType.BENEFIT_CATEGORY;
            personData2.atom_id = "1002";
            personData2.atom_state = true;
            personData2.atom_value = true;
            kSession.insert(personData2);
            // Атом 010012 Возраст
            personData2 = new PersonData(person2.person_pk);
            personData2.atom_type = AtomType.UZSFO;
            personData2.atom_id = "010012";
            personData2.atom_state = true;
            personData2.atom_value = Period.parse("P12Y");
            kSession.insert(personData2);
            
            // Другие ФЛ семьи
            Person person3 = new Person(3);
            kSession.insert(person3);
            // Атом ЛК 1002
            PersonData personData3 = new PersonData(person3.person_pk);
            personData3.atom_type = AtomType.BENEFIT_CATEGORY;
            personData3.atom_id = "1002";
            personData3.atom_state = true;
            personData3.atom_value = true;
            kSession.insert(personData3);
            // Атом 010012 Возраст
            personData3 = new PersonData(person3.person_pk);
            personData3.atom_type = AtomType.UZSFO;
            personData3.atom_id = "010012";
            personData3.atom_state = true;
            personData3.atom_value = Period.parse("P17Y");
            kSession.insert(personData3);
            // Атом 020018 Обучение
            personData3 = new PersonData(person3.person_pk);
            personData3.atom_type = AtomType.UZSFO;
            personData3.atom_id = "020018";
            personData3.atom_state = true;
            personData3.atom_value = true;
            kSession.insert(personData3); 
            
            // Другие ФЛ семьи
            Person person4 = new Person(4);
            kSession.insert(person4);
            // Атом ЛК 1003
            PersonData personData4 = new PersonData(person4.person_pk);
            personData4.atom_type = AtomType.BENEFIT_CATEGORY;
            personData4.atom_id = "1003";
            personData4.atom_state = true;
            personData4.atom_value = true;
            kSession.insert(personData4);
            // Атом 010012 Возраст
            personData4 = new PersonData(person4.person_pk);
            personData4.atom_type = AtomType.UZSFO;
            personData4.atom_id = "010012";
            personData4.atom_state = true;
            personData4.atom_value = Period.parse("P17Y");
            kSession.insert(personData4);
            // Атом 020018 Обучение
            personData4 = new PersonData(person4.person_pk);
            personData4.atom_type = AtomType.UZSFO;
            personData4.atom_id = "020018";
            personData4.atom_state = true;
            personData4.atom_value = true;
            kSession.insert(personData4);                  
            
            
            
    }

}
