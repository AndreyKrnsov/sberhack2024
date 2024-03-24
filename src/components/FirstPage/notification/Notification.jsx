import React from "react";
import cl from "./Notification.module.css"
import { CloseOutlined } from "@ant-design/icons";
import { Space } from "antd";

const Notification = ({text, isDeletable, id, sendDataToServer}) => {
    

    const handleDeleteClick = () => {
        sendDataToServer({ id: id });
    }
    return (
        <div className={cl.container}>
            <Space direction="vertical">
            {isDeletable && (
                    <button onClick={handleDeleteClick} className={cl.clsbtn}>
                        <CloseOutlined className={cl.clsicon} />
                    </button>
                )}
            <p className={cl.textP}>{text}</p>
            </Space>
        </div>
    )
}

export default Notification