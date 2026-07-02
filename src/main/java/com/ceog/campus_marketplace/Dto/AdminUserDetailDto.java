    package com.ceog.campus_marketplace.Dto;

    import com.ceog.campus_marketplace.Model.Type.RolesType;
    import lombok.*;

    import java.util.List;
    import java.util.Set;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class AdminUserDetailDto {
        private Long            id;
        private String          username;
        private String          college;
        private Set<RolesType>  roles;

        // product summary
        private int             totalListings;
        private int             activeListings;
        private int             soldListings;
        private List<ProductDto> products;

        // report summary
        private int             reportsMadeCount;
        private int             reportsReceivedCount;
        private List<ReportDto> reportsMade;
        private List<ReportDto> reportsReceived;
    }