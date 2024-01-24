import DefaultForm from "@/components/shared/form/ui/default-form";
import FormGroup from "@/components/shared/form/ui/form-group";
import FormSection from "@/components/shared/form/ui/form-section";
import {Button, Form, Input, message, Upload, UploadProps} from "antd";
import { useForm } from "antd/lib/form/Form";
import React, { useState } from "react";
import {UploadOutlined} from "@ant-design/icons";
import {createDefaultProfile, IDefaultProfileFormValue, updateDefaultProfile} from "@/client/default-profile";

interface IDefaultProfileFormProps {
  id?: string;
  initialValues?: Partial<IDefaultProfileFormValue>;
}

const DefaultProfileForm = ({ id, initialValues }: IDefaultProfileFormProps) => {
  const [form] = useForm();
  const [isLoading, setIsLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();


  const handleFinish = async (formValue: IDefaultProfileFormValue) => {
    try {
      setIsLoading(true);

      if (id) {
        await updateDefaultProfile(id, formValue);
        messageApi.success("수정되었습니다");
      } else {
        await createDefaultProfile(formValue);
        messageApi.success("생성되었습니다");
      }
    } catch (e: unknown) {
      messageApi.error("에러가 발생했습니다");
    } finally {
      setTimeout(() => setIsLoading(false), 500);
    }
  };

  const props: UploadProps = {
    name: 'image',
    beforeUpload: (file) => {
      const isPNG = file.type === 'image/png';
      if (!isPNG) {
        message.error(`${file.name} is not a png file`);
      }

      return false;
    },
    onChange: (info) => {
      return info.file;
    },
  };


  return (
      <>
        {contextHolder}
        <DefaultForm<IDefaultProfileFormValue>
            form={form}
            initialValues={{
              id: id,
              name: initialValues?.name
            }}
            onFinish={handleFinish}
        >
          <FormSection title="기본 프로필 수정 폼" description="기본 프로필 수정 기능">
            <FormGroup title="기본 프로필 이름">
              <Form.Item name="name">
                <Input.TextArea placeholder="기본 프로필 이름을 적어주세요." rows={1} />
              </Form.Item>
            </FormGroup>

            <FormGroup title="PNG 파일">
              <Form.Item name="image">
                <Upload action={'/wow/wow'} {...props}>
                  <Button icon={<UploadOutlined />}>Upload png only</Button>
                </Upload>
              </Form.Item>
            </FormGroup>

          </FormSection>

          <div className="text-center">
            <Button htmlType="submit" type="primary">
              제출
            </Button>
          </div>
        </DefaultForm>
      </>
  );
};

export default React.memo(DefaultProfileForm);
