import React from "react";
import cl from "./MyCheckbox.module.css"
import Checkbox from "antd/es/checkbox/Checkbox";
import { Button, Space, Radio } from "antd";
import { CloseOutlined } from "@ant-design/icons";

const MyCheckbox = ({ text, box, isMultiply, isDeletable, id, sendDataToServer }) => {    

    const handleDeleteClick = () => {
        sendDataToServer({ id: id });
    }
    return (
        <div className={cl.container}>
            <Space direction="vertical" className={cl.spc}>
                {isDeletable && (
                    <button onClick={handleDeleteClick} className={cl.clsbtn}>
                        <CloseOutlined className={cl.clsicon} />
                    </button>
                )}
                <p className={cl.textP}>{text}</p>
                <div>
                    {isMultiply ? (
                        box.map((item, index) => (
                            <div key={index}>
                                <Checkbox id={`checkbox-${index}`} className={cl.chbx} />
                                <label htmlFor={`checkbox-${index}`}>{item}</label>
                            </div>
                        ))
                    ) : (
                        <Radio.Group>
                            {box.map((item, index) => (
                                <div key={index}>
                                    <Radio value={item} className={cl.chbx} id={`checkbox-${index}`}>
                                        {item}
                                    </Radio>
                                    
                                </div>
                            ))}
                        </Radio.Group>
                    )}
                </div>

                <Button type="primary" className={cl.btn}>Отправить</Button>
            </Space>

        </div>
    )
}

export default MyCheckbox;