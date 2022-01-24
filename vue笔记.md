# vue笔记

 **VUE这个框架的数据流向是单向的**

所以数据绑定后的数据流向是从**vue实例**——>**DOM文档**的

* 数据绑定
```html
<html>
<head>
    <title>Vue Demo</title>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.16/dist/vue.js"></script>
</head>
<body>
    <div id="firstVue">
    {{my_data}}
    </div>
</body>
<script type="text/javascript">
    var myVue = new Vue({
        el:"#firstVue",
        data:{
            my_data: "test"
        }
    })
</script>
</html>
```
 如果想绑定某个HTML标签的属性值，就要用到`v-bind:`属性了，比如我想绑定一个标签是否可见的属性(hidden)，那么就应该这么写:
* 属性绑定
```html
<div id="firstVue" v-bind:hidden="my_hidden">{{my_data}}</div>
```
v-bind由于经常会用到，所以也可以缩写成冒号:
```html
<div id="firstVue" :hidden="my_hidden">{{my_data}}</div>
```
* 事件绑定

**`v-bind:`是用来绑定数据的，`v-on:`则是用来绑定事件的，比如我要绑定一个``的 `click`事件就这么写:**

```html
<button v-on:click="clickButton()">Click Me</button>
```

当然这里的*click*可以换成任意一个html事件，比如load , doubleclick , mouseon , mousedown这些，不过*click*肯定是我们最常用的。

将*click*动作绑定到clickButton()函数之后就需要实现这个函数了,我们要在之前的vue实例中加入新字段`methods`

```js
methods:{
        clickButton:function(){
            this.my_data = "Wow! I'm changed!"
        }
    }
```

需要加`this`后面直接写要引用的变量就可以了，如果不加`this`，系统会默认你想引用的是一个全局变量，可是这里我们需要引用的是这个vue实例里的局部变量。

**另外，`v-on:`语法同样有一个缩写`@`，比如`v-on:click="clickButton"`就等价于`@click="clickButton"**`

*  表单控件绑定

前面说Vue这个框架是单向数据传输的，就是从vue实例传送数据到DOM ，那么我们如何从DOM中实时获取用户输入的数据赋值给vue实例呢

这用到了Vue.js提供给用户的一个[语法糖](https://links.jianshu.com/go?to=https%3A%2F%2Fbaike.baidu.com%2Fitem%2F%E8%AF%AD%E6%B3%95%E7%B3%96%2F5247005%3Ffr%3Daladdin) `v-model` ,这个语法糖通过两步实现了数据的反向传递，也就是从DOM传送给vue实例数据：

 **第一步，**绑定了DOM标签的`input`事件(比如叫tapInput())

**第二步，**当用户进行输入时候，触发`tapInput()`函数，`tapInput()`函数内部读取此DOM标签的Value值，赋值给vue实例。