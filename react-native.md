1 高德地图使用

```
 生成发布版SHA1 
 在android/app/下载运行
keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
生成发布版的 key文件
keytool -list -keystore .\my-release-key.keystore 查看sha1的值
获取 packageName 在build.gradle文中的  applicationId "com.reactnativeapp"
```
2 获取定位
```
import {View,Text,StyleSheet,Image,PermissionsAndroid} from 'react-native'
import { init, Geolocation} from 'react-native-amap-geolocation'
 getLocation = async () => {
        await PermissionsAndroid.requestMultiple([
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
        ]);
        
        await init({
          ios: "",
          android: "69a6e31587f3c2bacbfaeb4946c64f5f"
        });
        
        Geolocation.getCurrentPosition((val) => {
          this.setState({
            position:val.location.city
          })
        });
      }
```
字体图标的使用
```
 1 安装插件 
 yarn add react-native-vector-icons
 react-native link react-native-vector-icons
 2 配置文件
 android/app/build.gradle 添加
 apply from: "../../node_modules/react-native-vector-icons/fonts.gradle"
 3 使用
 import Icon from 'react-native-vector-icons/FontAwesome';
 
 <Icon name="home" size={30} color="#FFF" />
 
```
 路由
 ```
 1 安装插件
 npm install @react-navigation/native
 
 npm install react-native-reanimated react-native-gesture-handler react-native-screens react-native-safe-area-context @react-native-community/masked-view
 
 使用堆栈
 yarn add @react-navigation/stack
 
 2使用
在入口文件 app.js中注册组件
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
const Stack = createStackNavigator();
<NavigationContainer>
  <Stack.Navigator
          screenOptions={{
            headerStyle: {
              backgroundColor: '#FF9880',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
            },
          }}
        >
        <Stack.Screen
          name="home"
          component={IndexPage}
          options={{
            title: '首页',
          }}
        />
        <Stack.Screen
          name="detail"
          options={{
            title: '商品详情',
          }}
          component={DetailPage}
        />
      </Stack.Navigator>
</NavigationContainer>


底部tabBar的使用

import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
 <Tab.Navigator initialRouteName='Home'
            tabBarOptions={{
              activeTintColor: '#FFF',
              activeBackgroundColor:'#FF9880'
            }}
            >
                <Tab.Screen name="Home"
                  style={{color:"gray"}}
                  component={HomePage}
                  options={{
                      tabBarLabel: '首页',
                      tabBarIcon: ({ color, size }) => (
                        <Icon name="home" size={30} color="#FFF" />
                      )
                    }}
                    />
                <Tab.Screen name="HomeScreen"
                 options={{
                  tabBarLabel: 'HomeScreen',
                  tabBarIcon: ({ color, size }) => (
                    <Icon name="home" size={30} color="#FFF" />
                  )
                }}
                component={HomeScreen} />
            </Tab.Navigator>

 ```
 使用redux
 ```
 1 yarn add redux react-redux
 2 创建仓库 store文件夹
  新增 index.js文件
  import {createStore} from 'redux';
    import reducer from './reducer'
    const store = createStore(reducer);
    
    export default store
    
新增 reducer文件
  const defaultStore = {
        name:'小明'
    }
    
    const reducer = function(state = defaultStore,action){
       switch(action.type){
           case 'add':
           state.numer ++
           break;
           case 'del':
           state.number ++
           break
       }
        return {...state}
    }
    
    export default reducer
3 入口文件配置 app.js
import {Provider} from 'react-redux'
import store from './src/store/index'


<Provider store={store}>
//代码
</Provider>

4 使用
import {connect} from 'react-redux'

const mapStateToProps=function(state){
    console.log(state)
    return{
        name:state.name
    }
}
const dispatchToProps = (dispatch)=>{
    return{
        add:()=>{
            dispatch({type:'add',obj:{a:'6'}})
        }
    }
}

export default connect(mapStateToProps,dispatchToProps)(DetailPage);

 ```

redux-thunk的使用
```
1 仓库的配置thunk
import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk'
import reducer from './reducer'
const store = createStore(reducer,applyMiddleware(thunk));

export default store

2 页面使用
作用 可以让disptach触发函数！从而可以异步操作
const dispatchToProps = function(dispatch){
    return{
        getShopList(){
            //调用dispatch方法
            dispatch(
                //dispatch内触发 函数
                (dispatch)=>{
                    req.get('http://data.zuidaku.com/Uniapp.uniapp/getGoodList', { 'page': 2 }).then(res => {
                        dispatch({
                            'type':'saveList',
                            'shopList':res.data
                        })
                    })  
                }
            );
        }
    }
}
```
1 创建目录
```
node > 12


npx react-native init my-app

cd AwesomeProject
yarn ios
# 或者
yarn react-native run-ios
```
简单案例
```
修改app.js文件

import React from 'react';
//引入基本组件
import {
//样式组件
StyleSheet,
//视图组件
View,
//文本组件
Text
} from 'react-native';
//创建样对象

const styles = StyleSheet.create({
view:{
height:200,
width:200,
//驼峰命名
backgroundColor:'rgba(200,255,0,0.5)'
}
})


//创建视图
const App = () =>{
return(
<>
<View Style = {Styles.view}>
//文件必须放在Text标签内
 <Text>Text内显示文字</Text>
</View>
</>
)
}


export default App
```
样式不会继承混合样式写在数组能 后边的样式可以覆盖前边的样式
```
import React from 'react';
import {SafeAreaView,View,Text,StyleSheet} from 'react-native';

//创建样式对象

const styles = StyleSheet.create({
	contanier:{
		height:200,
		backgroundColor:'green',
		alignItems:'center',
		justifyContent:'center',
		color:'red'
	},
	txt:{
		fontSize: 30,
        fontWeight: '600',
	}
})


class Demo3 extends React.Component{

 render(){
	 return(
	 <SafeAreaView>
	    <View style={styles.contanier}>
	          {/*没有样式继承，每个组件都要单独设置样式 */}
			 <View>
				<Text>样式不会继承</Text>
			 </View>
			 
			 <View style={{marginTop:30,backgroundColor:'orange'}}>
			 {/*混合样式写在数据内*/}
			     <Text style={[styles.txt,{color:'blue'}]}>设置样式属性混写在数组内</Text>
			 </View>
		 </View>
	 </SafeAreaView>
	 )
 }
	
}
export default Demo3
```
![截屏2020-05-27下午9.17.54.png](https://upload-images.jianshu.io/upload_images/16514325-71b5a2986c626d89.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


//阴影
	textShadow:{
		fontSize:40,
		textShadowColor:'red',
		textShadowOffset:{width:2,height:2},
		textShadowRadius:1
	}

组件的State 状态
```
import React from 'react';

import {StyleSheet,View,Text,SafeAreaView} from 'react-native';

class Demo6 extends React.Component{
	state = {
		name:'小明',
		type:1
	}
	
	changeType=() => {
	    this.setState({
		    type:this.state.type + 1,
		     name:'哈哈'
	    })
	}
	
	render(){
		const {name,type} = this.state
		return(
		<SafeAreaView>
			<View>
			  <Text>姓名：{name}</Text>
			  <Text>type:{type}</Text>
			  <View style={{width:200,height:100,backgroundColor:'skyblue',justifyContent:'center'}}>
{/*等于点击事件*/}
<Text onPress = {this.changeType}>按钮</Text></View>
			</View>
		</SafeAreaView>
		)
	}
	
}

export default Demo6
```
组件传值props
```
import React from 'react';

import {SafeAreaView,View,Text} from 'react-native';

class Demo7Child extends React.Component{
	constructor(props){
		super(props)
		this.state = {
			  //*prop接受
			name:props.name.username
		}
	}
	changeName =()=>{
		this.setState({
			name:this.state.name + '6'
		})
	}
	render(){
		const {name} = this.state
		return(
		<View>
		    <Text>props传值</Text>
		   <Text onPress = {this.changeName}>{name}</Text>
		</View>
		)
	}
}




const Demo7 = ()=>{
	return(
	 <SafeAreaView>
	     {/*传入数值*/}
	     <Demo7Child name={{username:'username'}} />
	 </SafeAreaView>
	)
}

export default Demo7
```
TextInput 组件的使用
```
import React from 'react';

import {SafeAreaView,View,Text,TextInput,TouchableOpacity,StyleSheet} from 'react-native';


const styles = StyleSheet.create({
   container: {
      paddingTop: 23
   },
   input: {
      margin: 15,
      paddingLeft:8,
      height: 40,
      borderColor: '#eeeeee',
      borderWidth: 1
   },
   submitButton:{
      backgroundColor: '#7a42f4',
      padding: 10,
      alignItems:'center',
      margin: 15,
      height: 40,
   },
   submitButtonText:{
      color: 'white'
   }
})


class Demo8 extends React.Component{
	constructor(props) {
	    super(props)
	    this.state = {
		    email:'',
		    password:'',
		    intro:'',
	    }
	}
	 handleEamil = (text) => {
      this.setState({ email: text })
   }
   handlePassword = (text) => {
      this.setState({ password: text })
   }

   handleIntro = (text) => {
      this.setState({ intro: text })
   }

   register = (email, pass,intro) => {
      alert('email: ' + email + '\npassword: ' + pass + "\nintro:" + intro)
   }
	render(){
		const {email,password,intro} = this.state
		return(
		<SafeAreaView style = {styles.container}>
		   <Text>InputText组件</Text>
		    <TextInput
		    //样式
		     style={styles.input} 
		     //提示语
		     placeholder = '请输入邮箱' 
		     //下划线的颜色,透明为 transparent
		     underlineColorAndroid = 'transparent'
		      //占位符的字体颜色
		      placeholderTextColor = "#ccc"
		      // 字母大写模式 sentences(默认) 每句话的第一字母
		                    // word 每个词的第一个字母
		                    // characters 全部大写
		      autoCapitalize = 'none'
		      // 键盘类型 default 默认键盘  numeric 纯数字
		                 // number-pad
		                 // email-address邮箱地址
		                 // phone-pad 手机
		      keyboardType = "email-address"
		     // 键盘上的返回键类型 done  go next search send
		     returnKeyType = 'next'
		     // 文本变化后的回调函数，参数为输入的文本
		     onChangeText = {this.handleEamil}
		     />
		     
		     <TextInput
		     style = {styles.input}
		     underlineColorAndroid = "transparent"
		     placeholder = "请输入密码"
		     placeholderTextColor = "#ccc"
		     autoCapitalize = "none"
		     returnType = "next"
		     //是否属于密码类型
		     secureTextEntry = {true}
		     onChangeText = {this.handlePassword}
		      />
		      
		    
		    <TextInput
		    style = {[styles.input,{height:100}]}
		    underlineColorAndroid = "transparent"
		    placeholder = "请输入描述"
		    placeholderTextColor = "#CCC"
		    autoCapitalize = "none"
		    //多行设置
		    nultiline = {true}
		    //行数
		    numberOfLines = {4}
		    //文字的位置靠上
		    textAlignVertical = 'top'
		    returnKeyType = 'done'
		    onChangeText = {this.handleIntro}
		    
		    />  
		     
		     {/*封装视图,使其可以正确响应触摸操作*/}
		     <TouchableOpacity
		     style = {styles.submitButton}
		     onPress = {
			     () => this.register(email,password,intro)
		     }>
		     <Text style={styles.submitButtonText}>注册</Text>
		     </TouchableOpacity>
		</SafeAreaView>
		)
	}
}


export default Demo8
```
图片组件 Image
```
import React from 'react';

import {SafeAreaView,View,Text,ScrollView,Image} from 'react-native'


class Demo9 extends React.Component{
	constructor(props){
		super(props)
		this.state = {
			 name:'小明'
		}
	}
	render(){
	  return(<SafeAreaView>
	    <ScrollView>
	      {/*普通图片*/}
	      <Image source={require('./sesstes/1.jpeg')} />
	      {/*网路图片*/}
	      <Image style={{margin:10,width:200,height:100}} source={{uri:'http://attach.bbs.miui.com/forum/201408/17/234450ybtkm8mdb2zmy2md.jpg'}} />
	      {/*图片模式contain */}
	      <Image style={{margin:10,width:200,height:200,resizeMode:'contain',borderWidth:1,borderColor:'red'}} source={require('./sesstes/1.jpeg')} />
	      {/*图片模式 cover*/}
	      <Image style={{margin:10,width:200,height:200,resizeMode:'cover',borderWidth:1,borderColor:'red'}} source={require('./sesstes/1.jpeg')} />
	      {/*图片模式  stretch*/}
	      <Image style={{margin:10,width:200,height:200,resizeMode:'stretch',borderWidth:1,borderColor:'red'}} source={require('./sesstes/1.jpeg')} />
	    </ScrollView>
	  </SafeAreaView>)
	}
}

export default Demo9
```
activityIndicator活动指示器 loading
```
import React from 'react';

import {SafeAreaView,View,Text,Button,StyleSheet,TouchableOpacity,ActivityIndicator} from 'react-native';

class Demo10 extends React.Component{
	constructor(props){
		super(props)
		this.state = {
			animating:true
		}
	}
	changeState = ()=>{
		this.setState({
			animating:!this.state.animating
		})
	}
	render(){
		const {animating} = this.state;
		return(
		<SafeAreaView>
		   <View>
		   <Text>指示器相当于 => Loading</Text>
		   </View>
		   <ActivityIndicator animating = {animating} color="#bc2b78" size = "large" >
		   </ActivityIndicator>
		   <Button title="按钮" onPress = {this.changeState}></Button>
		</SafeAreaView>
		)
	}
}

export default Demo10
```
Alert 对话框
```
import React from 'react';

import {Alert,TouchableOpacity, Modal,View,SafeAreaView,Text,Button,StyleSheet} from 'react-native';

class Demo11 extends React.Component{
	constructor(props){
		super(props)
		this.state = {
			
		}
	}
	showAlert1 = ()=>{
		Alert.alert('发送成功')
	}
	showTip = ()=>{
		Alert.alert('删除成功')
	 }
	showAlert2 = ()=>{
	  Alert.alert(
	  '警告',
	  '确认删除',
	  [
	  {text:'确认1',onPress:()=>this.showTip()},
	  {text:'取消3',onPress:() => 'cancel'},
	  {text:'取消2',onPress:() => 'cancel'},
	  {cancelable:false}
	  ]
	  )
	}
	render(){
		return(
		<SafeAreaView>
		  <Text>对话框</Text>
		  <View style={{alignItems:'center'}}>
		   <TouchableOpacity style={styles.button} onPress = {this.showAlert1}><Text>发送</Text></TouchableOpacity>
		   <TouchableOpacity style={styles.button} onPress = {this.showAlert2}><Text>删除</Text></TouchableOpacity>
		  </View>
		</SafeAreaView>
		)
	}
}

const styles = StyleSheet.create({
	button:{
		backgroundColor:'#4ba37b',
		width:100,
		height:50,
		borderRadius:50,
		justifyContent:'center',
		alignItems:'center',
		marginTop:100
	}
})

export default Demo11
```
AysncStorage 异步存储 使用 async await 或者 then()
```
import React from 'react';

import {SafeAreaView,AsyncStorage,View,Text,TextInput,Alert,TouchableHighlight,StyleSheet} from 'react-native';

class Demo12 extends React.Component{
	state = {
		name:'小明',
		value:'我不是小明'
	}
	async readName(){
		try{
			const value = await AsyncStorage.getItem('name');
			if(value !== null){
				this.setState({name:value})
			}
			Alert.alert('数据读取成功')
		}catch(e){
			console.log(e)
			Alert.alert('数据读取失败')
		}
	}
	async setName(){
		await AsyncStorage.setItem('name',this.state.value);
		Alert.alert('保存成功')
	}
	render(){
		return(
		<SafeAreaView>
		 <View><Text>异步存储 AsyncStorage</Text></View>
		 <TextInput style = {styles.textinput} autoCapitalize = "none" value = {this.state.name} />
		 <View style={{flexDirection:'row'}}>
		   <TouchableHighlight style={[styles.button,{marginRight:8}]} onPress = {this.setName.bind(this)}>
		    <Text style={styles.buttonText}>保存</Text>
		   </TouchableHighlight>
		   <TouchableHighlight style={[styles.button,{marginRight:8}]} onPress = {this.readName.bind(this)}>
		    <Text style={styles.buttonText}>读取</Text>
		   </TouchableHighlight>
		 </View>
		 <View>
		   <Text>当前内容：{this.state.name}</Text>
		 </View>
		</SafeAreaView>
		)
	}
}


const styles = StyleSheet.create({
	container:{
		margin:10
	},
	textInput:{
		margin:5,
		height:44,
		width:'100%',
		borderWidth:1,
		borderColor:'#dddddd'
	},
	button:{
	 flex:1,
	 height:44,
	 justifyContent:'center',
	 alignItems:'center',
	 width:100,
	 backgroundColor:'red'
	},
	buttonText:{
	 justifyContent:'center',
	 color:'#FFF'
	}
})
export default Demo12
```
动画Animated
```
import React from 'react';
import {SafeAreaView,View,StyleSheet,Animated,TouchableOpacity} from 'react-native';

class Demo13 extends React.Component{
	UNSAFE_componentWillMount = ()=>{
		//创建动画属性 初始值为50
		this.animateWidth = new Animated.Value(50)
		this.animatedHeight = new Animated.Value(100)
	}
	animatedBox =()=>{
		// 点击后，设置动画变化
		Animated.timing(this.animateWidth,{
			toValue:200,
			duration:1000,
			useNativeDriver: false
		}).start()
		Animated.timing(this.animatedHeight,{
			toValue:200,
			duration:500,
			useNativeDriver: false
		}).start()
	}
	render(){
		// 讲动画数值变化绑定到样式
		const animatedStyle = {
			width:this.animatedWidth,
			height:this.animatedHeight
		}
		return(
		<SafeAreaView>
		  <TouchableOpacity
		  style = {styles.container}
		  onPress = {this.animatedBox}
		  >
		  <Animated.View style = {[styles.box,animatedStyle]} />
		  </TouchableOpacity>
		</SafeAreaView>
		)
	}
}


const styles = StyleSheet.create({
	contanier:{
		justifyContent:'center',
		alignItems:'center'
	},
	box:{
		backgroundColor:'blue',
		width:50,
		height:100
	}
})

export default Demo13
```
开关组件 Switch 
```
import React from 'react';
import {SafeAreaView,View,Text,Switch,StyleSheet} from 'react-native';

class Demo14 extends React.Component{
	constructor(props){
		super(props)
		this.state = {
			onoff:true
		}
	}
	toggleValue=(val)=>{
		// 传入 true false
		this.setState({
			onoff:val
		})
	}
	render(){
		return(<SafeAreaView>
		  <View style={{justifyContent:'center',alignItems:'center',marginTop:50}}>
		    <Text>Switch开关</Text>
		      //改变事件
		    <Switch onValueChange = {this.toggleValue} value={this.state.onoff} />
		    <Text>开关{this.state.onoff ? '开':'关'}</Text>
		  </View>
		</SafeAreaView>)
	}
}

export default Demo14
```
导航栏
```
import React, { Component } from 'react';
import {View, Text, StatusBar, StyleSheet, TouchableOpacity} from 'react-native'

class App extends Component {

   state = {
      hidden:false,
      barStyle:'default'
   }

    changeHidden = () =>{

      var hidden = this.state.hidden ? false : true;
        this.setState({ hidden: hidden })
    }

    changeBarStyle = () =>{
      //  主题色
      var  barStyle = this.state.barStyle == 'light-content' ? 'dark-content' : 'light-content';
        this.setState({ barStyle: barStyle })
    }

    render() {
      return (
         <View>        //风格                            显示隐藏
            <StatusBar barStyle = {this.state.barStyle} hidden={this.state.hidden} />
            <TouchableOpacity style={styles.button} onPress = {this.changeHidden}>
               <Text>显示或隐藏</Text>
           </TouchableOpacity>
           <TouchableOpacity style={styles.button} onPress = {this.changeBarStyle}>
               <Text>改变主题色</Text>
           </TouchableOpacity>
        </View>
      )
   }
}
export default App

const styles = StyleSheet.create ({
    button: {
        backgroundColor: '#4ba37b',
        width: 100,
        borderRadius: 50,
        alignItems: 'center',
        marginTop: 100
    }
})
```
选择器Picker
```
import React from 'react';

import {SafeAreaView,View,Text,Picker,StyleSheet} from 'react-native';

class Demo16 extends React.Component{
	users = [
      {label: '请选择性别',value:''},
      {label: '男',value:'male'},
      {label: '女',value:'female'},
      {label: '其它',value:'other'}
   ]
   state = {user:''}
   updateUser = (val1,val2) => {
	   //获取val1 位置 value  val2 为索引
	   this.setState({
		   user:val1
	   })
   }
   render(){
	   return(<SafeAreaView>
	      <View style = {styles.contanier}>
	        <Text style = {styles.label}>选择性别</Text>
	        <Picker selectedValue = {this.state.user} onValueChange = {this.updateUser}>
	           {
		           this.users.map((item,index) => {
			         return(<Picker.Item key={index} label = {item.label} value = {item.value} />)  
		           })
	           }
	        </Picker>
	        <Text style = {styles.label}> 你的选择是</Text>
	        <Text style = {styles.text}>{this.state.user}</Text>
	      </View>
	   </SafeAreaView>)
   }
}


const styles = StyleSheet.create({
	contanier:{
		 margin:50,
	},
	label:{
		fontSize:14,
		color:'#333333'
	},
	text:{
		fontSize:30,
		alignSelf:'center',
		color:'red'
	}
})

export default Demo16
```