<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!--表头-->
<table border="0" cellspacing="0" cellpadding="0"
	style="width: 99%; margin-left: 2px;">
	<tr style="height: 40px;">
		<th width='60%'><input style="width: 14px; height: 14px;margin-left: 6px;"
			type="checkbox" name="ckAll" id="ckAll" /> <span>文件名</span>
		</th>
		<th width='16%'><span>大小</span></th>
		<th width='24%' class="orderby time" data-order="timeAsc"><span style="margin-left: 10px;">修改时间</span></th>
	</tr>
</table>
<div class="myTabBox" style="overflow: auto;height: 273px;">
	<table class="table table-hover" border="0" cellspacing="0"
		cellpadding="0" style="margin: 0;">
		<tr class="headerTr">
			<td width="60%"></td>
			<td width="16%"></td>
			<td width="24%"></td>
		</tr>
		<script type="text/javascript">
        	$(function () {
	            var $table = $(".myTabBox .table"),
	            	$ckAll = $("#ckAll"),	//全选按钮;
	            	$nowtr = $(".headerTr");	//创建文件
	            $table.on('click', ".headerTr~tr", function () {
	            	var $ck = $(this).find("[name=ck]");
	                if ($ck.attr("ckval") == 1) {
	                	$ck.attr("ckval", 0);
	                	$ck.prop("checked", false);
	                } else {
	                    $ck.attr("ckval", 1);
	                    $ck.prop("checked", true);
	                }
	                if ($table.find(".headerTr~tr").length == $table.find(".headerTr~tr input[name=ck]:checked").length) {
	                    $ckAll.prop("checked", true);
	                } else {
	                    $ckAll.prop("checked", false);
	                }
	            }).on('dblclick',".headerTr~tr", function () {
	            	if($(this).attr("data-type")==1){
	                	getMyFile($(this).attr("data-id"),"timeAsc");
	            	}
	            });
	            $ckAll.click(function () {
	                var isChecked = $(this).prop("checked");
	                if (isChecked == true) {
	                    $table.find("input[name=ck]").attr("ckval", 1);
	                } else {
	                    $table.find("input[name=ck]").attr("ckval", 0);
	                }
	                $table.find(".headerTr~tr input[name=ck]").prop("checked", isChecked);
	            });
	            
	            //返回上一级
	            $(".head.bread-h").on('click',function(){
	            	getMyFile($(this).attr("parentDir"),"timeAsc");
	            });
	            $("#myFileHeader .breadcrumb").on('click',"li:not(:last)",function(){
	            	getMyFile($(this).attr("parentDir"),"timeAsc");
	            });
	            
	            $("th.orderby").click(function(){
	            	if($(this).hasClass("time")){
	            		if($(this).attr("data-order")=="timeAsc"){
	            			$(this).attr("data-order","timeDesc")
	            		}else{
	            			$(this).attr("data-order","timeAsc")
	            		}
	            		getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),$(this).attr("data-order"));
	            	}
	            });
	        });
            /*
				当 fileType 为空时 
					得到指定目录（parentDir）下的文件夹和文件
				否者
					按文件类型进行查询
				order : 排序方式
            */
            function getMyFile(parentDir,order) {
            	var $myFileHeader = $("#myFileHeader"),
            		$bread=$myFileHeader.find(".breadcrumb");
            	//路径导航
      			$bread.empty().show();
      			$(".headerTr").hide();//隐藏创建文件夹的节点
      			
      			$("#ckAll").prop("checked",false);//全选按钮
	           	var $table = $(".myTabBox .table");
               	$table.find(".headerTr").siblings().remove();
           	 	$("#myFile>nav").show();
                var fileUrl="rest/folio/"+parentDir+"?order="+order+"&isNotDel=1";
                	$.ajax({
                     	url:fileUrl,
                     	type:"GET",
                     	beforeSend:function(){
                     		openSpinner("拼命加载中！！！");
                     	},success:function(data){
	      					/*路径导航 begin*/
	               			var $liAll=$('<li parentDir="0">全部文件</li>');
	      					if(data.catalog.length>0){
	      						$liAll.html('<a href="javascript:void(0)">全部文件</a>');
	      						$(".bread-h").show().attr("parentDir",data.catalog[data.catalog.length-1].parentDir).siblings(".head").hide();;     	
	      					}else{
	      						$liAll.addClass("active");
	      						$(".head").hide();
	      					}
	                 		$bread.append($liAll);
	                 			
	               			$.each(data.catalog,function(i,obj){
	               				var $li=$('<li parentDir="'+obj.id+'"><a href="javascript:void(0)">'+obj.folioName+'</a></li>');
	               				if(i==data.catalog.length-1){
	               					$li.addClass("active").html(obj.folioName).siblings("li").removeClass("active");
	               				}
	               				$bread.append($li);
	               			});
                 			/*路径导航 end*/
                 			
                 			$myFileHeader.find("div.last").html("已全部加载，共"+data.files.length+"个");
                 			if(data.files.length==0){
                 				$table.append('<tr class="emptyTr text-center"><td colspan="3" style="height:60px;line-height:60px;">'+
                 						'您还没上传过文件哦，点击上传选择文件按钮～'+
                 					'</td><tr>');
                 			}
                 			$.each(data.files, function (i, obj) {
                 				var $tr = $("<tr data-id='"+obj.id+"' data-type='"+obj.type+"' data-parentDir='"+obj.parentDir+"'><td width='60%'><input type='checkbox' data-id='" + obj.id + "' name='ck' value='' />" +
                                         "<i class='fa "+obj.clazz+"'></i>" +
                                         "<span>" + obj.showName + "</span></td>" +
                                         "<td width='16%'>" + obj.size + "</td><td width='24%'>" + obj.createTime + "</td></tr>");
                            	$table.append($tr);
                            });
                 		},error:function(e){
                 			alert("错误"+e.responseText);
                 		},complete:function(){
                 			closeSpinner();
                 		}
                     });//ajax
                 }
             </script>
	</table>
</div>