package project.DxWorks.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.common.ui.Response;
import project.DxWorks.user.dto.request.ModifyUserInfRequestDto;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public Response<UserInfResponseDto> getUserInf(@PathVariable Long userId){
        return userService.getUserInf(userId);
    }

    @PutMapping("{userId}")
    public Response<String> modifyUserEmail(@PathVariable Long userId, @RequestBody ModifyUserInfRequestDto requestDto){
        return userService.modifyUserEmail(userId, requestDto);
    }
}
