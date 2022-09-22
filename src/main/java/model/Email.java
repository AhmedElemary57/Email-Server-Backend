package model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class Email {
    private String _id;
    private String priority;
    private String date;
    private String sender;
    private String receiver;
    private String subject;
    private String body;
    private boolean seen;
    private List<String> attachments;

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    private String attachmentPath;
    private String[] attachmentsName;
    
    public Email() {
        this.priority = "b";
        this.attachmentPath = "noAttachment";
    }
    public Email(String priority,
                 String date,
                 String sender,
                 String receiver,
                 String subject,
                 String body) {
        this.priority = priority;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Email(String priority,
                 String date,
                 String sender,
                 String receiver,
                 String subject,
                 String body,
                 String attachmentPath) {
        this.priority = priority;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.attachmentPath = attachmentPath;
    }
    
    public String[] getAttachmentsName() {
        return attachmentsName;
    }
    public void setAttachmentsName() {
        File file = new File("data/attachments/"+attachmentPath);
        attachmentsName = file.list();
    }
    public String getAttachmentPath() {
        return attachmentPath;
    }
    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getSubject() {
        return subject;
    }
    public String getBody() {
        return body;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
