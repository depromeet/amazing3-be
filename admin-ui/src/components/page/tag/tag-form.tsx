import DefaultForm from "@/components/shared/form/ui/default-form";
import FormGroup from "@/components/shared/form/ui/form-group";
import FormSection from "@/components/shared/form/ui/form-section";
import {Button, Form, Input, message} from "antd";
import { useForm } from "antd/lib/form/Form";
import React, { useState } from "react";
import {ITagFormValue, updateTag, createTag} from "@/client/tag";

interface ITagFormProps {
  id?: string;
  initialValues?: Partial<ITagFormValue>;
}

const TagForm = ({ id, initialValues }: ITagFormProps) => {
  const [form] = useForm();
  const [isLoading, setIsLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();


  const handleFinish = async (formValue: ITagFormValue) => {
    try {
      setIsLoading(true);

      if (id) {
        await updateTag(id, formValue);
        messageApi.success("수정되었습니다");
      } else {
        await createTag(formValue);
        messageApi.success("생성되었습니다");
      }
    } catch (e: unknown) {
      messageApi.error("에러가 발생했습니다");
    } finally {
      setTimeout(() => setIsLoading(false), 500);
    }
  };

  return (
      <DefaultForm<ITagFormValue>
          form={form}
          initialValues={{
            id: id,
            content: initialValues?.content
          }}
          onFinish={handleFinish}
      >
        <FormSection title="태그 수정 폼" description="목표에 설정 가능한 태그 수정 기능">
          <FormGroup title="태그 이름">
            <Form.Item name="content">
              <Input.TextArea placeholder="태그 이름을 적어주세요." rows={1} />
            </Form.Item>
          </FormGroup>


        </FormSection>

        <div className="text-center">
          <Button htmlType="submit" type="primary">
            제출
          </Button>
        </div>
      </DefaultForm>
  );
};

export default React.memo(TagForm);
