package strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
 
public Object parse(String input) {
   	
	List<Object> list = null;
	Map<String, Object> map = null;
	Object object = null;
	
	//Check to see if we get simple data.
	if(input.startsWith(" [") && input.endsWith("] ")){
		list = new ArrayList<Object>();
		input = input.replaceAll(" \\[", "").replaceAll("\\] ", "").replaceAll("\"", "").replace(" ","");
		String[] parts = input.split(",");
		for(String str : parts){
			if(!Character.isDigit(str.charAt(0))){
				list.add(new String(str));
			}else{
				list.add(new Double(str));
			}
		}
	
	object = list;
	
	//Check to see if we get complex data.
	} else if(input.startsWith("{") && input.endsWith("}")){
		input = input.replaceFirst("\\{","").replaceAll(" ","").replaceAll("\\}", "").replaceAll("\\]", "").replaceAll("\"", "");
		map = (Map)getFormattedList(input);
		object = map;	
	}
	
	return object;
  }

  public Object getFormattedList(String str){
	  Map<String, Object> map = new HashMap<String, Object>(); 
	  List<Object> list = new ArrayList<Object>();
	  String sb = ""; int i;
	 
	  char[] strArray = str.toCharArray();
	  for(i=0; i<str.length();i++){
		  if(strArray[i]=='{' || strArray[i]=='[' ){
			  break;
		  }
		 
		  sb = sb + strArray[i];
	  }
	
	   if(i==str.length() && !str.contains(":")){
		  String[] parts = sb.split(",");
		  for(String str1 : parts){
			  list.add(str1);
		  }
		 
		  return list;
	  }
	
	  String[] parts = sb.split(",");
	  for(String str1 : parts){
		  String[] innerParts = str1.split(":");
		  if(innerParts.length>1){
		     map.put(innerParts[0], innerParts[1]);
		  }
		  else{
			 map.put(innerParts[0], getFormattedList(str.substring(i+1)));
		  }
	  }
	  return map;
  }

	
  public static void main(String[] args) {
    final JsonParser jsonParser = new JsonParser();
    
    System.out.println("Simple Double Data with Single Json Object");
    
    Object result = jsonParser.parse(" [ 10, 20, 30.1 ] ");
    if (result != null) {
      List<Object> list = (List<Object>) result;
      for (int i = 0; i < 3; i++) {
        System.out.println(list.get(i));
      }
    }

    System.out.println("Mutilpe types of Data with Single Json Object");
    result = jsonParser.parse(" [ 10 , 20, \"hello\", 30.1 ] ");
    if (result != null) {
      List<Object> list = (List<Object>) result;
      for (int i = 0; i < 4; i++) {
        System.out.println(list.get(i));
      }
    }

    System.out.println("Data with Object Types");
    result = jsonParser.parse("{" +
        " \"hello\": \"world\"," +
        " \"key1\": 20," +
        " \"key2\": 20.3," +
        " \"foo\": \"bar\"" +
        "}");
    
    if (result != null) {
      Map<String, Object> map = (Map<String, Object>) result;
      System.out.println(map.get("hello"));
      System.out.println(map.get("key1"));
      System.out.println(map.get("key2"));
      System.out.println(map.get("foo"));
    }
   
    System.out.println("Data with Nested Object Types");
    result = jsonParser.parse("{ \"hello\" : \"world\" , \"key1\" : 20 , \"key2\": 20.3 , \"foo\": { \"hello1\": \"world1\",\"key3\": [200,300] } }");
    
    if (result != null) {
      Map<String, Object> map = (Map<String, Object>) result;
      System.out.println(map.get("hello"));
      System.out.println(map.get("key1"));
      System.out.println(map.get("key2"));
      Map<String, Object> nestedMap = (Map<String, Object>) map.get("foo");
      System.out.println(nestedMap.get("foo"));
      List<Object> nestedList = (List<Object>) nestedMap.get("key3");
      for (int i = 0; i < 2; i++) {
        System.out.println(nestedList.get(i));
      }
    }
  }
}
