

function moviewrite(){
            //폼태그 가져오기
            var formData = new FormData(form);
            alert(formData);
            alert($("mvid").val());
            $.ajax({
                type:"post",
                url:"/admin/moviewritecontroller",
                data:
                    formData
                ,
                processData: false,
                contentType: false,
                success: function(data){
                    alert(data);
                }
            })
}