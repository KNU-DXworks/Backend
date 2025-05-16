package project.DxWorks.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.common.ui.Response;
import project.DxWorks.user.dto.request.InterestUserDto;
import project.DxWorks.user.dto.request.ModifyUserInfRequestDto;
import project.DxWorks.user.dto.response.InterestUserListResponseDto;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Response<UserInfResponseDto> getUserInf(@RequestAttribute Long userId){
        return userService.getUserInf(userId);
    }

    @PutMapping
    public Response<String> modifyUserEmail(@RequestAttribute Long userId, @RequestBody ModifyUserInfRequestDto requestDto){
        return userService.modifyUserEmail(userId, requestDto);
    }

    @PostMapping("/interest")
    public Response<String> interestUser(@RequestAttribute Long userId, @RequestBody InterestUserDto dto) {
        return userService.interestUser(userId, dto.toUser());
    }

    @GetMapping("/interest/to")
    public Response<List<InterestUserListResponseDto>> getInterestToUser(@RequestAttribute Long userId){

        return userService.getInterestUser(userId, true);
    }

    @GetMapping("/interest/from")
    public Response<List<InterestUserListResponseDto>> getInterestFromUser(@RequestAttribute Long userId){

        return userService.getInterestUser(userId, false);
    }
}
