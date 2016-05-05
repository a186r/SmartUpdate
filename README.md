# SmartUpdate
android app增量更新
##增量更新原理:
old包与new包通过二进制比较,得到两者的差异包patch,patch包放在服务器,用户客户端升级的时候直接下载patch包和当前安装的old包合并出一个新的包,用户直接安装合并后的包即可,整个过程建议加入MD5校验
##windows本地测试:
  下载bsdiff4.3-win32解压后得到两个exe文件,bsdiff.exe用来本地获得patch,bspatch.exe用来做本地patch包和old包的合并.
命令行格式:
  
    bsdiff.exe old.apk finalnew.apk patch.apk
    bspatch.exe old.apk mergenew.apk patch.apk
    
    patch.apk是bsdiff.exe之后得到的差分包
    finalnewapk是最新发布版本
    mergenew.apk是old.apk与patch.apk合成的包
    
在加入自己app的时候可以自己使用jni,也可以直接使用本项目中的so文件,使用的时候不要更改调用so的类的包名
##注意事项:

	1. 版本跨度较大的,建议整包升级
	2. 假设已经发布了三个版本v1.0,v2.0,v3.0,现在要在后台发布v4.0,上传时就应该生成1.0-->4.0的差异包,2.0-->4.0的差异包,3.0-->4.0的差异包
	3. 选择下面这个开源二进制比较工具实现 http://www.daemonology.net/bsdiff/ ,下载后得到bsdiff-4.3.tar.gz,其中bsdiff.c是二进制文件比对的代码;bspatch.c是二进制文件合成的代码;
	4. 使用bsdiff、bspatch时，还需用到bzip2 http://www.bzip.org/downloads.html 下载后得到:bzip2-1.0.6.tar.gz
	5. 将bzip2-1.0.6.tar.gz里面的文件拷贝到jni目录下,然后调用bsdiff生成差异包,调用bspatch合成新包
