# coursera-dl下载coursera上的课程视频

## 安装python3.8的版本

## pip安装coursera-dl

```bash
 python -m pip install coursera-dl
```

### 修改hosts文件

```bash
#添加下面这句
52.84.167.78 d3c33hcgiwev3.cloudfront.net
```

### 编写下载配置文件

比如你要把视频下载到coursera_download目录下，就在这个目录下新建coursera-dl.conf,并按以下格式填写信息：

```bash
--username XXXX@gmail.com
--password XXXXX
--subtitle-language en,zh-CN
--download-quizzes
--cauth EaGb30YcNwQmRC......
```

其中的cauth值：

<img src="C:\Users\hwadee\AppData\Roaming\Typora\typora-user-images\image-20220118105556474.png" alt="image-20220118105556474" style="zoom:50%;" />

<img src="C:\Users\hwadee\AppData\Roaming\Typora\typora-user-images\image-20220118105624115.png" alt="image-20220118105624115" style="zoom:50%;" />![image-20220118105642950](C:\Users\hwadee\AppData\Roaming\Typora\typora-user-images\image-20220118105642950.png)

<img src="C:\Users\hwadee\AppData\Roaming\Typora\typora-user-images\image-20220118105624115.png" alt="image-20220118105624115" style="zoom:50%;" />

获得值：

<img src="C:\Users\hwadee\AppData\Roaming\Typora\typora-user-images\image-20220118105642950.png" alt="image-20220118105642950" style="zoom:50%;" />

### 切换到coursera_download目录下执行命令：

```bash
#cryptocurrency是课程名称
coursera-dl cryptocurrency
```

假如下载中途退出了或出于其他原因需要继续上次下载时

```bash
coursera-dl cryptocurrency --resume
```

更多信息和命令参数相关可参考官方文档：https://github.com/coursera-dl/coursera-dl#windows