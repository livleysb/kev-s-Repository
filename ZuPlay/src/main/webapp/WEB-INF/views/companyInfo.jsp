<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div id="company-${masterDTO.isuCd}" class="company-window">
	<div class="company-header">기업정보 - ${masterDTO.isuCd}</div>
	<input type="hidden" value="${masterDTO.isuCd}" class="company-isuCd">
	<input type="hidden" value="${plQuantity}" class="company-qty">
	<div class="company-content">
		<div class="row-fluid">
			<div class="col-xs-12 company-title">
				<span class="company-title-name">${masterDTO.isuKorAbbrv}</span> <span
					class="company-title-stock">${masterDTO.priceDTO.trdPrc}</span>
			</div>
		</div>
		<div class="row-fluid">
			<div class="col-xs-6 company-chart-warpper">
				<div class="company-chart">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab"
							href="#company-chart-today">오늘</a></li>
						<li><a data-toggle="tab" href="#company-chart-month">한달</a></li>
					</ul>

					<div class="tab-content">
						<div id="company-chart-today" class="tab-pane fade in active">
							<%-- <c:forEach items="${masterDTO.rtpList}" var="rtp" varStatus="stu">
						   		${rtp.rpTrdTm2} : ${rtp.rpTrdPrc}
						   </c:forEach> --%>
						</div>
						<div id="company-chart-month" class="tab-pane fade">
							<%-- <c:forEach items="${masterDTO.dpList}" var="dp" varStatus="stu">
						   		${dp.dpDate2} : ${dp.dpClsprc}
						   </c:forEach> --%>
						</div>
					</div>
				</div>
			</div>

			<div class="col-xs-6">
				<table class="table table-condensed">
					<tbody>
						<tr>
							<th>전일비</th>
							<td><fmt:formatNumber
									value="${masterDTO.priceDTO.cmpprevddPrc}" /></td>
							<th>등락</th>
							<td>${masterDTO.priceDTO.fluctuationRate}%</td>
						</tr>
						<tr>
							<th>전일</th>
							<td><fmt:formatNumber
									value="${masterDTO.priceDTO.trdPrc - masterDTO.priceDTO.cmpprevddPrc}" /></td>
							<th>거래량</th>
							<td><fmt:formatNumber
									value="${masterDTO.priceDTO.accTrdvol}" /></td>
						</tr>
						<tr>
							<th>저가</th>
							<td><fmt:formatNumber value="${masterDTO.priceDTO.lwprc}" /></td>
							<th>고가</th>
							<td><fmt:formatNumber value="${masterDTO.priceDTO.hgprc}" /></td>
						</tr>
						<tr>
							<th>시가</th>
							<td><fmt:formatNumber value="${masterDTO.priceDTO.opnprc}" /></td>
							<th>시가총액</th>
							<td><fmt:formatNumber
									value="${masterDTO.listShrs * masterDTO.priceDTO.trdPrc/100000000}" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<hr>
		<div class="company-buy">
			<div class="row-fluid">
				<div class="col-xs-12"><div class='company-buy-slider'></div></div>
			</div>
				<div class="row-fluid">
				<div class="col-xs-4"><div class="company-buy-input"></div></div>
				<div class="col-xs-5"><h4 class="company-buy-value"></h4>0</div>
				<div class="col-xs-3"><button class="company-buy-btn btn btn-block btn-success">매수</button></div>
			</div>
		</div>
		<div class="company-sell">
			<div class="row-fluid">
				<div class="col-xs-12"><div class='company-sell-slider'></div></div>
			</div>
			<div class="row-fluid">
				<div class="col-xs-4"><div class="company-sell-input"></div></div>
				<div class="col-xs-5"><h4 class="company-sell-value">0</h4></div>
				<div class="col-xs-3"><button class="company-sell-btn btn btn-danger btn-block">매도</button></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$("#company-${masterDTO.isuCd}")
			.ready(
					function() {

						var chartData = function(data,kind) {
							console.log(data);
							chartJson = new Array();
							
							$.each(data, function(index, item) {
								var ChartObj = new Object();
								if(kind=="rtp"){
									ChartObj.x = item.rpTrdTm2+32400000;
									ChartObj.y = item.rpTrdPrc;
									ChartObj.name = item.rpTrdTm2;
								}else{
									ChartObj.x = item.dpDate2+32400000;
									ChartObj.y = item.dpClsprc;
									ChartObj.name = item.dpDate2;
								}
									chartJson.push(ChartObj);
							})

							kind=="rtp" ? companylineChart(chartJson,"#company-${masterDTO.isuCd} #company-chart-today") : 
										  companylineChart(chartJson,"#company-${masterDTO.isuCd} #company-chart-month");

						}

						/*라인차트 그리기*/
						var companylineChart = function(chartJson,chartId) {
							console.log(chartId);
							$(chartId).highcharts(
											{
												chart : {
													zoomType : 'x',
													height : 200,
													width : 300
												},
												title : {
													text: ' '
												},
												xAxis : {
													type : 'datetime'
												},
												yAxis : {
													title : {
														text : 'Profit'
													}
												},
												legend : {
													enabled : false
												},
												tooltip : {
													formatter : function() {
														var result = new Date(this.x).toUTCString().split("GMT")[0];
														return result+ "<br>"+ this.series.name+ ' : <b>'+ (this.y).format()+ '원</b>';
													}
												},
												plotOptions : {
													area : {
														fillColor : {
															linearGradient : {
																x1 : 0,
																y1 : 0,
																x2 : 0,
																y2 : 1
															},
															stops : [
																	[
																			0,
																			Highcharts
																					.getOptions().colors[0] ],
																	[
																			1,
																			Highcharts
																					.Color(
																							Highcharts
																									.getOptions().colors[0])
																					.setOpacity(
																							0)
																					.get(
																							'rgba') ] ]
														},
														marker : {
															radius : 2
														},
														lineWidth : 1,
														states : {
															hover : {
																lineWidth : 1
															}
														},
														threshold : null
													}
												},

												series : [ {
													type : 'area',
													name : '체결가',
													data : chartJson
												} ]
											});
						}
						
						/*탭 클릭 이벤트*/
				         $("#company-${masterDTO.isuCd} .nav-tabs  a").on("click",function(){
				        	 console.log("#company-${masterDTO.isuCd} 탭클릭")
				            if($(this).text()=="오늘"){
				            	console.log("오늘")
				               $("#company-${masterDTO.isuCd} #company-chart-today").empty();
				               chartData(JSON.parse('${rtpList}'),"rtp"); 
				            }else{
				            	console.log("한달")
				               $("#company-${masterDTO.isuCd} #company-chart-month").empty();
				            	chartData(JSON.parse('${dpList}'),"dp")
				            }
				         }) 
						chartData(JSON.parse('${rtpList}'),"rtp"); 
 					});
</script>
