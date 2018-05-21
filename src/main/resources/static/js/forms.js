$(function(){
	$.ajax({
		url:'all',
		type:'GET',
		success:function(data){				
			for(var i=0;i<data.length;i++){
				var $input=$("<input type='button'  class='btn btn-default'  onclick='goForm(this)'>");
				$input.attr('data',data[i].table_name);
				$input.val(data[i].table_comment+"("+data[i].table_name+')');
				$('.main').append($input);
			}
		}
	});
		
});
function goForm(btn){
	window.location.href='detail?tableName='+$(btn).attr('data');
}