package deals

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import grails.converters.*
import org.codehaus.groovy.grails.web.json.*

class WhereIsMyContentController {

    def index = { }
	
	
	Flap getModules(String host,String pos,String locale,String url){

	Flap flap = new Flap();
    List<Module> modules = new ArrayList<Module>();
    String templateName="";
	String fUrl = 'http://'+host+':10000/deals/debug/module/flap/'+pos+'/'+locale+'/255/'+url;
	println fUrl
	def http = new HTTPBuilder(fUrl)

     http.request( GET, TEXT ) { req ->
	
     response.success = { resp, reader ->
       println resp.statusLine
	   assert resp.statusLine.statusCode == 200
       JSONObject userJson = JSON.parse(reader)
        if(userJson.flapStats !=null){
		userJson.flapStats.each{
          Module mod = new Module();
          mod.moduleName = it.key; 
          String error = it.value.error;
          if(error=="null"){
            mod.hasError=false;
          } else if(error.contains("EmptyModuleException")){
            mod.hasError=true;
            mod.errorLabel="Content Not found";
          }else{
            mod.hasError=true;
            mod.errorLabel="Others";
          }
          mod.error = it.value.error;
          mod.solrQuery = it.value.cdsQuery.solrServerQuery
          //templateName = it.value.pageParams.template
		  String cdsQuery = generateCdsQuery(it.value.cdsQuery,host)
		  cdsQuery = "http://cds-002.cpeg.orbitz.net:8686/cds/dataView/showData?"+ cdsQuery
		  mod.cdsQuery = cdsQuery		  
		  modules.add(mod)
        }
	templateName = userJson.pageParams.template
    }
	}
    }
	flap.modules = modules;
	flap.templateName = templateName;
	return flap;
	}
	def showData={
	
	String url = params.url;
	String pos = params.pos;
	String locale = params.locale;
	Flap flapProd = getModules('deals-001.cpeg.orbitz.net',pos,locale,url);
	Flap flapPrev = getModules('deals-cmsref-001.cpeg.orbitz.net',pos,locale,url);
	List<Module> prodModules = flapProd.modules; 
	List<Module> previewModules = flapPrev.modules;
	render(view:'index',model:[prodModList:prodModules,previewModList:previewModules,prodTemplate:flapProd.templateName,previewTemplate:flapPrev.templateName])
}
	String generateCdsQuery(JSONObject cds,String host){
		String cdsQuery = "";
		cds.each{
			String key = it.key;
			//key.contains("urlMapping")
			if(key.equals("status")|| key.equals("locale") || key.equals("pos") || key.equals("urlType")){
				cdsQuery =cdsQuery+"&"+ it.key +"="+it.value;
			}
			if(key.equals("query")){
				String q = it.value;
				q = q.replace("AND","~")
				def qAnd = q.split('~')
				for (str in qAnd){
				 str = str.trim().replace("'","");
				 cdsQuery = cdsQuery+"&"+str.replace(":","=")
				}
			}
			
	
		}
		if(host.contains("cmsref")){
			cdsQuery = cdsQuery+"&host=indexedsearch.prod.o.com:8980"
		}else{
		 	cdsQuery = cdsQuery+"&host=reviewsearch.prod.o.com"
		}
		return cdsQuery
	}
}

class Flap{

List<Module> modules;
String templateName;
}
class Module{

String solrQuery;
String error;
String moduleName;
String errorLabel;
boolean hasError;
String cdsQuery;
}
