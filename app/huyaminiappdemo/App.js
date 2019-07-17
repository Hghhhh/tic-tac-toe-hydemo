import React, { Component } from 'react';
import {  View,Button,Text,StyleSheet } from 'react-native';
import hyExt from 'hyext-rn-sdk';

class Square extends Component{
  render(){
    return (
      <Button style={styles.square} onPress={this.props.onClick} title={this.props.value}>
      </Button>
    );
  }
}


class Board extends Component {

  renderSquare(i) {
    return <Square onClick={()=>this.props.onClick(i)} value={this.props.squares[i]?this.props.squares[i]+"":" "}/>;
  }

  render() {
    return (
      <View style={{

        flexDirection: 'column',
        justifyContent: 'space-between',
      }}>
      <View style={{flexDirection: 'row'}}>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(0)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(1)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(2)}
        </View>
      </View>
      <View style={{flexDirection: 'row'}}>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(3)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(4)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(5)}
        </View>
      </View>
      <View style={{flexDirection: 'row'}}>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(6)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(7)}
        </View>
        <View style={{width: 50, height: 50, backgroundColor: 'powderblue'}} >
          {this.renderSquare(8)}
        </View>
      </View>
    </View>
    );
  }
}

export default class huyaminiappdemo extends Component {  
    constructor(props){
      super(props);
      this.state={
          history:[{
              squares:Array(9).fill(null),
          }],
          stepNumber : 0,
          xIsNext:true,
      };
      hyExt.logger.info('开始加载');       
      hyExt.observer.on('ROOMOPEN', ROOMOPEN => {
        hyExt.logger.info(ROOMOPEN);  
        this.jumpTo(0);
      });
      hyExt.observer.on('POINT', point => {
        hyExt.logger.info('point');
        this.handleClick2(point);
      });
  }
  calculateWinner(squares) {
    const lines = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
      const [a, b, c] = lines[i];
      if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
        return squares[a];
      }
    }
    return null;
  }
  handleClick2(i) {
    const history = this.state.history.slice(0,this.state.stepNumber+1);
    const current = history[history.length - 1];
    const squares = current.squares.slice();
    if (this.calculateWinner(squares) || squares[i]) {
        return;
    }
    squares[i] = this.state.xIsNext ? 'O' : 'X';
    this.setState({
    history: history.concat([{
        squares: squares,
    }]),
    stepNumber:history.length,
    xIsNext: !this.state.xIsNext,
    });
  }
  handleClick(i) {
      const history = this.state.history.slice(0,this.state.stepNumber+1);
      const current = history[history.length - 1];
      const squares = current.squares.slice();
      const xIsNext = this.state.xIsNext;
      if (this.calculateWinner(squares) || squares[i] ) {
          return;
      }
      squares[i] = this.state.xIsNext ? 'O' : 'X';
      this.setState({
      history: history.concat([{
          squares: squares,
      }]),
      stepNumber:history.length,
      xIsNext: !this.state.xIsNext,
      });
      hyExt.logger.info(i);
      hyExt.requestEbs({
        header: { 'x-header': 'foo' },
        host: 'www.guohanghuang.cn',
        port: 8082,
        path: '/point?x='+i,
        httpMethod: 'POST',
        param: { 'x':""+i}
      }).then(({ res, msg, ebsResponse: { entity, statusCode, header } }) => {
        hyExt.logger.info(statusCode)
      }).catch(err => {
        hyExt.logger.warn('调用失败', err)
      });
  }
  jumpTo(step) {
      this.setState({
        stepNumber: step,
        xIsNext: (step % 2) === 0,
      });
    }
    
  render() {
      const history = this.state.history;
      const moves = history.map((step, move) => {
          const desc = move ?
            'Go to move #' + move :
            'Go to game start';
          return (
            <li key={move}>
              <Button onPress={() => this.jumpTo(move)}>
              {desc}
              </Button>
            </li>
          );
        });
      const current = history[this.state.stepNumber];
      const winner = this.calculateWinner(current.squares);
      let status;
      if (winner) {
          status = 'Winner: ' + winner;
      } else {
          status = 'Next player: ' + (this.state.xIsNext ? 'O' : 'X');
      }
    return (
      <View>
        <View >
          <Board onClick={(i)=>this.handleClick(i)} squares={current.squares}/>
        </View>
        <View >
          <Text>{status}</Text>
        </View>
      </View>
    );
  }
}


const styles = StyleSheet.create({


  square:{
    color: '#fff',
    width: 34,
  }

})