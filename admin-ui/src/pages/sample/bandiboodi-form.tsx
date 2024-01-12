import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import DefaultForm from "@/components/shared/form/ui/default-form";
import FormGroup from "@/components/shared/form/ui/form-group";
import FormSection from "@/components/shared/form/ui/form-section";
import {Button, Form, Input, message, Upload, UploadProps} from "antd";
import { useForm } from "antd/es/form/Form";
import { useState } from "react";
import {UploadOutlined} from "@ant-design/icons";

const pageHeader: IPageHeader = {
  title: "폼",
};

interface ISampleFormValue {}

const FormPage: IDefaultLayoutPage = () => {
  const [form] = useForm();
  // formData는 아래에 전송데이터를 보여주기위한 state값. 실제 작업시 지우고 사용.
  const [formData, setFormData] = useState<ISampleFormValue>();

  const handleFinish = (formValue: ISampleFormValue) => {
    // 이곳에 실제 폼 전송 로직 작성.
    setFormData(formValue);
    form.resetFields();
  };

  const props: UploadProps = {
    beforeUpload: (file) => {
      const isPNG = file.type === 'image/png';
      if (!isPNG) {
        message.error(`${file.name} is not a png file`);
      }
      return isPNG || Upload.LIST_IGNORE;
    },
    onChange: (info) => {
      console.log(info.fileList);
    },
  };

  return (
    <>
      <DefaultForm<ISampleFormValue>
        form={form}
        initialValues={{
        }}
        onFinish={handleFinish}
      >
        <FormSection title="스티커 추가 폼" description="목표에 설정 가능한 스티커(이미지)를 추가하는 폼">

          <FormGroup title="스티커 이름">
            <Form.Item name="name">
              <Input.TextArea placeholder="스티커 이름을 적어주세요. (파일 명과는 무관합니다)" rows={1} />
            </Form.Item>
          </FormGroup>

          <FormGroup title="PNG 파일">
            <Form.Item name="image">
              <Upload {...props}>
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

      <DefaultForm<ISampleFormValue>
          form={form}
          initialValues={{
          }}
          onFinish={handleFinish}
      >
        <FormSection title="태그 추가 폼" description="목표에 설정 가능한 태그를 추가하는 폼">

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

    </>
  );
};

FormPage.getLayout = getDefaultLayout;
FormPage.pageHeader = pageHeader;

export default FormPage;
