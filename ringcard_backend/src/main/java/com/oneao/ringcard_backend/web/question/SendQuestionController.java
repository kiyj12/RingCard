package com.oneao.ringcard_backend.web.question;

import com.oneao.ringcard_backend.domain.question.QuestionSendDto;
import com.oneao.ringcard_backend.domain.user.User;
import com.oneao.ringcard_backend.domain.question.Question;
import com.oneao.ringcard_backend.service.AnswerService;
import com.oneao.ringcard_backend.service.UserService;
import com.oneao.ringcard_backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static java.sql.Types.NULL;


@Controller
@RequiredArgsConstructor
@RestController
public class SendQuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PostMapping({"question/{questionId}/anony"})
    public String addQuestion1(@PathVariable Long questionId, Question question, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        // 이미 띄워져있는 question의 정보
        Question beforeQuestion= questionService.findByIdNoAuth(questionId).get();
        Long userId = beforeQuestion.getUserId();
        question.setUserId(userId);
        Question savedQuestion = questionService.save(question);
        redirectAttributes.addAttribute("questionId", beforeQuestion.getId());
        redirectAttributes.addAttribute("status",true);

        return "redirect:/question/{questionId}/anony";
    }

    @PostMapping({"userHome/{username}"})
    public void addQuestion2(@PathVariable String username,   HttpServletRequest request, @RequestBody QuestionSendDto requestBody) {
        User user = userService.findByUsername(username).get();

        String questionContents = requestBody.getQuestionContents();
        String questionHyperlink = requestBody.getQuestionHyperlink();
        Integer questionNoteType = requestBody.getNoteType();
        Integer questionTapeType = requestBody.getTapeType();

        if (questionNoteType == null) {
            questionNoteType = 0;
        }
        if (questionTapeType == null) {
            questionTapeType = 0;
        }

        Long userId = user.getId();
        Question question = new Question(questionContents, questionHyperlink, userId, false, false, false, questionNoteType, questionTapeType);

        System.out.println("question = " + question);
        Question savedQuestion = questionService.save(question);
        System.out.println(savedQuestion);
        System.out.println(user);

    }
}
