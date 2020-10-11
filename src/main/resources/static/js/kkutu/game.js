export default class Game {
    roundReady = (data) => {
        this._NOT_OVERRIDDEN('roundReady(data)');
    }

    turnStart = (data) => {
        this._NOT_OVERRIDDEN('turnStart(data)');
    }

    turnGoing = () => {
        this._NOT_OVERRIDDEN('turnGoing()');
    }

    turnEnd = (id, data) => {
        this._NOT_OVERRIDDEN('turnEnd(id, data)');
    }

    // declare a warning generator to notice if a method of the interface is not overridden
    // Needs the function name of the Interface method or any String that gives you a hint ;)
    _NOT_OVERRIDDEN = (functionName = '알 수 없음') => {
        console.log(`%c경고: "${this.constructor.name}" 클래스에서 함수 "${functionName}" (이)가 오버라이딩 되지 않았습니다.`, 'background-color: #E53935; color: white; font-size: 12pt;');
        throw Error(`"${this.constructor.name}" 클래스에서 함수 "${functionName}" (이)가 오버라이딩 되지 않았습니다.`)
    }
}