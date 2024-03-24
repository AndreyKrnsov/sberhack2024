import React, { useEffect } from "react";
import Notification from "../components/FirstPage/notification/Notification";
import cl from "./FirstPage.module.css"
import { Space } from "antd";
import Message from "../components/FirstPage/message/Message";
import Rate from "../components/FirstPage/rate/Rate";
import MyCheckbox from "../components/FirstPage/myCheckbox/MyCheckbox";
import { useState } from "react";
import axios from "axios";


const FirstPage = () => {
    const [dataSource, setDataSource] = useState([]);
    // const dataSource = [];

    useEffect(() => {        
        axios.get('http://192.168.142.223:8080/api/main')
            .then(res => {
                setDataSource(res.data);
            })
            .catch(err => {
                console.error(err);                
            })
    }, [])



    const sendDataToServer = (data) => {
        const url = 'http://192.168.142.223:8080/api/deleteElement'; 

        axios.post(url, data)
            .then(response => {
                console.log('Данные успешно отправлены:', response);
                console.log(data)               
            })
            .catch(error => {
                console.error('Ошибка при отправке данных:', error);                
            });
    };

    const renderComponent = (item) => {
        const componentKey = `component-${item.id}`;
        switch (item.type) {
            case 'Notification':
                return <Notification key={item.id} text={item.text} isDeletable={item.isDeletable} id={item.id} sendDataToServer={sendDataToServer} />;
            case 'Message':
                return <Message key={item.id} text={item.text} sender={item.sender} isDeletable={item.isDeletable} id={item.id} sendDataToServer={sendDataToServer} />;
            case 'Rate':
                return <Rate key={item.id} text={item.text} countOfButton={item.countOfButton} isDeletable={item.isDeletable} id={item.id} sendDataToServer={sendDataToServer} />;
            case 'Checkbox':
                return <MyCheckbox key={item.id} text={item.text} isMultiply={item.isMultiply} box={item.box} isDeletable={item.isDeletable} id={item.id} sendDataToServer={sendDataToServer} />;
            case 'ElementsList':
                return (
                    <div key={componentKey} className={cl.elementsList}>
                        {item.elements.map((nestedItem, index) => renderComponent(
                            nestedItem,
                        ))}
                    </div>
                );
            default:
                return null;
        }
    };


    return (
        <div className={cl.container}>
            <Space direction="vertical">
                {dataSource.map((el) => renderComponent(el))}
            </Space>
        </div>
    )
}


export default FirstPage