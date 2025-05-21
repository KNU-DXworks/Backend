package project.DxWorks.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.common.ui.Response;
import project.DxWorks.user.dto.response.mainpage.MainPageResponseDto;
import project.DxWorks.user.service.MainPageService;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping
    public Response<MainPageResponseDto> mainPage(@RequestAttribute Long userId){

        return mainPageService.mainPage(userId);
    }


}
