public DataResult<DLFileInfo> appUploadFile() {
		BufferedReader reader = null;
		System.out.println("上传用户id--》"+CurrentUser.getUserId());
		try {
			HttpServletRequest request = ServiceContextThreadLocal.getServiceContext().getRequest();
			ServiceContextThreadLocal.getServiceContext().setUserId(96414);
			//DataInputStream inputStream = new DataInputStream(request.getInputStream());
//	      1.判断当前request消息实体的总长度
	        int totalBytes = request.getContentLength();
//	      2.在消息头类型中找出分解符,例如:boundary=----WebKitFormBoundaryeEYAk4vG4tRKAlB6
	        String contentType = request.getContentType();
	        int position = contentType.indexOf("boundary=");
	        String startBoundary = "--" + contentType.substring(position+"boundary=".length());
	        String endBoundary = startBoundary + "--";
	        //将request的输入流读入到bytes中
	        InputStream inputStream = request.getInputStream();
	        DataInputStream dataInputStream = new DataInputStream(inputStream);
	        byte[] bytes = new byte[totalBytes];
	        dataInputStream.readFully(bytes);
	        dataInputStream.close();
	        //将字节读入到字符流中
	        reader = new BufferedReader(new StringReader(new String(bytes)));
	        //开始读取reader(分割form表单内的表单域类型:文本或者文件)
	        //记录当前的读取行对应的bytes;
	        int temPosition = 0;
	        int end = 0;
	     
            //当读取一次文件信息后
           
            bytes = subBytes(bytes, end, totalBytes);
            temPosition = 0;
            reader = new BufferedReader(new StringReader(new String(bytes))); 
            
            //读取一行的信息:------WebKitFormBoundary5R7esAd459uwQsd5,即:lastBoundary
            String str = reader.readLine();
            //换行算两个字符
            temPosition += str.getBytes().length + 2;
            //endBoundary:结束
            if(str==null||str.equals(endBoundary)){
            	return DataResult.errByMessage("not fount file");
            }
            //表示头信息的开始(一个标签,input,select等)
            if(str.startsWith(startBoundary)){
                //判断当前头对应的表单域类型

                str = reader.readLine(); //读取当前头信息的下一行:Content-Disposition行
                temPosition += str.getBytes().length+2;

                int position1 = str.indexOf("filename="); //判断是否是文件上传
                //such as:Content-Disposition: form-data; name="fileName"; filename="P50611-162907.jpg"
                if(str.indexOf("filename=") != -1){//表示是普通文本域上传
                	  //解析当前上传的文件对应的name(input标签的name),以及fieldname:文件名
                    int position2 = str.indexOf("name=");
                    //去掉name与filename之间的"和;以及空格
                    String name = str.substring(position2 + "name=".length() + 1, position1-3);
                    //去掉两个"
                    String filename = str.substring(position1 + "filename=".length() + + 1,str.length() - 1);

                    //读取行,such as:Content-Type: image/jpeg,记录字节数,此处两次换行
                    temPosition += (reader.readLine().getBytes().length + 4);
                    end = this.locateEnd(bytes, temPosition, totalBytes, endBoundary);
                    String path = request.getSession().getServletContext().getRealPath("/");
                   /* DataOutputStream dOutputStream = new DataOutputStream(new FileOutputStream(new File("D://test1.jpg")));
                    dOutputStream.write(bytes, temPosition, end-temPosition-2);
                    dOutputStream.close();*/
                	/*
        		    request.getContentType();*/
                    
                    
        		    DLFileInfo dlFileInfo = DLFileUtil.saveFile(filename,subBytes(bytes, temPosition, end-temPosition-2));
        		    
        		    return DataResult.successByDatas(dlFileInfo);
                }
            }
	        
			
			
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		return DataResult.errByMessage("server error 传");
	}