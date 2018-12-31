import React, {Component} from 'react';
import {StyleSheet, Text, View, Button, NativeModules} from 'react-native';
const AddShortcut = NativeModules.AddShortcut;

const places = [
  {
    name: 'Android',
    imageUrl: 'https://sdtimes.com/wp-content/uploads/2017/03/android-1904852_1920.jpg',
    deepLink: 'fb://'
  },
  {
    name: 'Android Studio',
    imageUrl: 'https://sdtimes.com/wp-content/uploads/2018/09/image1a.png',
    deepLink: 'fb://'
  },
  {
    name: 'Apple',
    imageUrl: 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/Apple-Apple.svg/1000px-Apple-Apple.svg.png',
    deepLink: 'fb://'
  }
];

export default class App extends Component {
  constructor() {
    super();
  }

  componentDidMount() {
    this.createDynamicShortcut();
  }

  createDynamicShortcut = () => {
    AddShortcut.setDynamicShortcuts(places);
  };

  createPinnedShortcut = () => {
    AddShortcut.setPinnedShortcuts(places[0]);
  };

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Sample app for shortcuts</Text>
        <Text style={styles.instructions}>Long tap on the app icon</Text>
        <Button title={`Shortcut`} onPress={this.createPinnedShortcut}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
