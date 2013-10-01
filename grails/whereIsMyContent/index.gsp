<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Data Viewer</title>
    </head>
    <body>


        <div class="body">
            <g:form action="showData" method="get" >
            		<br/>
		   <div style="width:600px;">
			<table>
			<tr class="prop">
			  <td class="name">
                <label for="pos">POS</label>
               <input type="text" id="pos" name="pos" value="${params.pos}" required="true" size="4"/>
              </td>
			<td class="name">
                <label for="url">Locale:</label>
               <input type="text" id="locale" name="locale" value="${params.locale}" required="true" size="5"/>
              </td>
			<td class="name">
                <label for="url">Url:</label>
               <input type="text" id="url" name="url" value="${params.url}" required="true" size="60" />
              </td>
			<td><input type="submit" id="submit" value="submit"/>
			</tr>
			</table>
		   </div>
		 </g:form>	
	<br/>
		<!-- Main content -->
		<div class="container">
		  <div class="divleft">
			<h3>Production</h3>
			<table>
				<tr><td><h4>Template:${prodTemplate}</h4></td></tr>
				<g:each in="${prodModList}" status="i" var="mod">
				  	<g:if test="${i%2==0}">
						<g:set var='style' value='odd'/>
					</g:if>
					<g:else>
						<g:set var='style' value='even'/>
					</g:else>
					<g:if test="${mod.hasError}">
						<g:set var='style' value='error'/>
					</g:if>
					<tr class="${style}">
						<td>
							<div>
							    <h4>${mod.moduleName}</h4>
								<g:if test="${mod.hasError}">
								   Error : ${mod.errorLabel} <br/>
								</g:if>
								<a href="${mod.solrQuery}">Solr Query</a> 
							</div>
						</td>
					</tr>
				</g:each>
			</table>
		</div>
		<div class="divright">
			<h3>Preview</h3> 
			<table>
				<tr><td><h4>Template:${previewTemplate}</h4></td></tr>
                <g:each in="${previewModList}" status="i" var="mod">
                    <g:if test="${i%2==0}">
                        <g:set var='style' value='odd'/>
                    </g:if>
                    <g:else>
                        <g:set var='style' value='even'/>
                    </g:else>
                    <g:if test="${mod.hasError}">
                        <g:set var='style' value='error'/>
                    </g:if>
                    <tr class="${style}">
                        <td>
                            <div>
                                <h4>${mod.moduleName}</h4>
                                <g:if test="${mod.hasError}">
                                   Error : ${mod.errorLabel} <br/>
                                </g:if>
                                <a href="${mod.solrQuery}">Solr Query</a> 
                            </div>
                        </td>
                    </tr>
                </g:each>
            </table>

		</div>

		</div>
	<div>
	<p>&nbsp;</p>
	</div>	
	</body>
</html>
